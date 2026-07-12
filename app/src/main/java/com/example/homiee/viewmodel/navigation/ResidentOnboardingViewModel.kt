package com.example.homiee.viewmodel.navigation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.model.ResidentAddressRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.ResidentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ResidentOnboardingViewModel : ViewModel() {

    private val repository = ResidentRepository()

    // ── Address ──
    var houseNo by StateHolder(); private set
    var area by StateHolder(); private set
    var city by StateHolder(); private set
    var pincode by StateHolder(); private set
    var latitude by StateHolder("0.0"); private set
    var longitude by StateHolder("0.0"); private set

    fun onHouseNoChange(v: String) { houseNo = v }
    fun onAreaChange(v: String) { area = v }
    fun onCityChange(v: String) { city = v }
    fun onPincodeChange(v: String) { pincode = v }
    fun updateLocation(lat: String, lng: String) { latitude = lat; longitude = lng }

    private val _addressShowErrors = MutableStateFlow(false)
    val addressShowErrors: StateFlow<Boolean> = _addressShowErrors.asStateFlow()

    private val _addressLoading = MutableStateFlow(false)
    val addressLoading: StateFlow<Boolean> = _addressLoading.asStateFlow()

    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError.asStateFlow()

    fun submitAddress(context: Context, nextStepRoute: String, onSuccess: () -> Unit) {
        if (houseNo.isBlank() || area.isBlank() || city.isBlank() || pincode.isBlank()) {
            _addressShowErrors.value = true
            _addressError.value = "Please fill in all required fields."
            return
        }
        _addressShowErrors.value = false
        _addressLoading.value = true
        _addressError.value = null
        viewModelScope.launch {
            val result = repository.saveAddress(
                ResidentAddressRequest(houseNo, area, city, pincode, latitude, longitude)
            )
            _addressLoading.value = false
            when (result) {
                is ApiResult.Success -> {
                    // CHANGED: persist the next step so that if the app is killed
                    // before onboarding finishes, Splash resumes here instead of
                    // restarting from the address form. Splash reads this via
                    // tokenManager.getCurrentStep().
                    TokenManager(context).saveCurrentStep(nextStepRoute)
                    onSuccess()
                }
                is ApiResult.Error -> _addressError.value = result.message
            }
        }
    }

    // ── Photo ──
    private val _photoShowErrors = MutableStateFlow(false)
    val photoShowErrors: StateFlow<Boolean> = _photoShowErrors.asStateFlow()

    private val _photoLoading = MutableStateFlow(false)
    val photoLoading: StateFlow<Boolean> = _photoLoading.asStateFlow()

    private val _photoError = MutableStateFlow<String?>(null)
    val photoError: StateFlow<String?> = _photoError.asStateFlow()

    fun submitPhoto(context: Context, imageUri: Uri?, onSuccess: () -> Unit) {
        if (imageUri == null) {
            _photoShowErrors.value = true
            _photoError.value = "Please upload a profile photo to continue."
            return
        }
        _photoShowErrors.value = false
        _photoLoading.value = true
        _photoError.value = null
        viewModelScope.launch {
            try {
                // CHANGED: compress/downscale on a background thread before upload.
                // Raw camera/gallery photos can be 5-20MB, which nginx rejects with
                // HTTP 413 (default client_max_body_size is often just 1MB). Scaling
                // to a max dimension and re-encoding as JPEG keeps uploads small
                // (typically a few hundred KB) regardless of server config.
                //
                // We use context.applicationContext here (not the raw context passed
                // in) so this still works even if the coroutine resumes after the
                // hosting Activity has been torn down/recreated.
                val file = withContext(Dispatchers.IO) {
                    compressImageToFile(context.applicationContext, imageUri)
                }
                val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("profile_photo", file.name, requestBody)
                val result = repository.uploadPhoto(part)
                _photoLoading.value = false
                when (result) {
                    is ApiResult.Success -> {
                        // CHANGED: this is the real source of truth Splash checks
                        // (tokenManager.areFormsCompleted()) — without this, the
                        // app has no memory of onboarding being done and Splash
                        // sends the resident back into the forms on every relaunch.
                        TokenManager(context).markFormsCompleted()
                        onSuccess()
                    }
                    is ApiResult.Error -> _photoError.value = result.message
                }
            } catch (e: Exception) {
                // CHANGED: log the *actual* exception. Filter logcat by tag
                // PHOTO_UPLOAD_ERROR to see exactly what failed.
                android.util.Log.e("PHOTO_UPLOAD_ERROR", "compress/upload failed", e)
                _photoLoading.value = false
                _photoError.value = when (e) {
                    is SecurityException ->
                        "We no longer have permission to read that photo. Please pick it again."
                    is java.io.IOException ->
                        "That photo couldn't be opened. It may be a cloud-only or " +
                                "unavailable photo — please pick a different one, such as a photo " +
                                "taken directly with the camera."
                    is OutOfMemoryError ->
                        "That image is too large to process. Please choose a smaller photo."
                    else ->
                        "Couldn't read the selected image. Please try again."
                }
            }
        }
    }

    /**
     * Lets the resident bypass the photo step entirely. This is a purely local
     * action (no network call) — it marks onboarding as complete via
     * [TokenManager] so Splash sends the resident to Home from now on, then
     * invokes [onSuccess] to navigate there immediately.
     */
    fun skipOnboarding(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            TokenManager(context).markFormsCompleted()
            onSuccess()
        }
    }

    /**
     * Decodes the picked image via Coil and re-encodes it as a downscaled JPEG.
     *
     * CHANGED: this previously used ContentResolver.openInputStream() +
     * BitmapFactory directly. That failed with FileNotFoundException for some
     * Photo Picker (content://media/picker/...) URIs on certain OEM ROMs (seen
     * on Vivo/FunTouch), even though the exact same URI rendered fine on-screen
     * via Coil's rememberAsyncImagePainter. Rather than fight OEM-specific
     * ContentResolver quirks, we now reuse Coil's own decode pipeline — the
     * same one already proven to load the image successfully in the picker
     * preview — to get the Bitmap, then compress that ourselves.
     *
     * Note: we deliberately do NOT call MediaStore.setRequireOriginal() here.
     * That resolves a picker URI to the real underlying MediaStore URI, which
     * requires the READ_MEDIA_IMAGES permission to read — a permission this
     * app doesn't request, since using the Photo Picker is specifically meant
     * to avoid needing it. Calling it caused a SecurityException instead of
     * fixing anything.
     */
    private suspend fun compressImageToFile(
        context: Context,
        uri: Uri,
        maxDimensionPx: Int = 1080,
        quality: Int = 80
    ): File {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .size(maxDimensionPx, maxDimensionPx)
            .allowHardware(false) // need a software Bitmap so we can compress/save it
            .build()

        val result = loader.execute(request)
        if (result !is SuccessResult) {
            val cause = (result as? ErrorResult)?.throwable
            if (cause is Exception) throw cause
            throw java.io.IOException("Coil failed to load image for $uri", cause)
        }

        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
            ?: throw java.io.IOException("Unexpected drawable type decoding $uri")

        val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
        }
        return file
    }
}

/** Tiny delegate so `by StateHolder()` gives a mutableStateOf-backed String field with an equals-based setter. */
private class StateHolder(initial: String = "") {
    private val state = androidx.compose.runtime.mutableStateOf(initial)
    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): String = state.value
    operator fun setValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>, value: String) { state.value = value }
}
package com.example.homiee.viewmodel.navigation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.model.EmergencyContactRequest
import com.example.homiee.data.model.ResidentAddressRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.ResidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun submitAddress(onSuccess: () -> Unit) {
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
                is ApiResult.Success -> onSuccess()
                is ApiResult.Error -> _addressError.value = result.message
            }
        }
    }

    // ── Emergency contact ──
    var contactName by StateHolder(); private set
    var mobileNumber by StateHolder(); private set

    fun onContactNameChange(v: String) { contactName = v }
    fun onMobileNumberChange(v: String) { mobileNumber = v }

    private val _emergencyShowErrors = MutableStateFlow(false)
    val emergencyShowErrors: StateFlow<Boolean> = _emergencyShowErrors.asStateFlow()

    private val _emergencyLoading = MutableStateFlow(false)
    val emergencyLoading: StateFlow<Boolean> = _emergencyLoading.asStateFlow()

    private val _emergencyError = MutableStateFlow<String?>(null)
    val emergencyError: StateFlow<String?> = _emergencyError.asStateFlow()

    fun submitEmergencyContact(onSuccess: () -> Unit) {
        if (contactName.isBlank() || mobileNumber.isBlank()) {
            _emergencyShowErrors.value = true
            _emergencyError.value = "Please fill in all required fields."
            return
        }
        _emergencyShowErrors.value = false
        _emergencyLoading.value = true
        _emergencyError.value = null
        viewModelScope.launch {
            val result = repository.saveEmergencyContact(
                EmergencyContactRequest(contactName, mobileNumber)
            )
            _emergencyLoading.value = false
            when (result) {
                is ApiResult.Success -> onSuccess()
                is ApiResult.Error -> _emergencyError.value = result.message
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
                val file = uriToFile(context, imageUri)
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("profile_photo", file.name, requestBody)
                val result = repository.uploadPhoto(part)
                _photoLoading.value = false
                when (result) {
                    is ApiResult.Success -> onSuccess()
                    is ApiResult.Error -> _photoError.value = result.message
                }
            } catch (e: Exception) {
                _photoLoading.value = false
                _photoError.value = "Couldn't read the selected image. Please try again."
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Unable to open image")
        val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        return file
    }
}

/** Tiny delegate so `by StateHolder()` gives a mutableStateOf-backed String field with an equals-based setter. */
private class StateHolder(initial: String = "") {
    private val state = androidx.compose.runtime.mutableStateOf(initial)
    operator fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): String = state.value
    operator fun setValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>, value: String) { state.value = value }
}
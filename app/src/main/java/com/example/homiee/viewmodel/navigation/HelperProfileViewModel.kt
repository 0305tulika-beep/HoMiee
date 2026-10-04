package com.example.homiee.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.model.HelperDetailDto
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import com.example.homiee.ui.screens.Residentflow.HelperProfileData
import com.example.homiee.ui.screens.Residentflow.ServiceWithPrice
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HelperProfileViewModel(
    private val helperId: String,
    private val repository: BookingRepository
) : ViewModel() {

    var isLoading by mutableStateOf(true); private set
    var errorMessage by mutableStateOf<String?>(null); private set
    var helper by mutableStateOf<HelperProfileData?>(null); private set

    init {
        load()
    }

    fun load() {
        // The API needs the numeric helper_id (e.g. 12).
        val id = helperId.toIntOrNull()
        if (id == null) {
            isLoading = false
            errorMessage = "Couldn't open this profile."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            when (val result = repository.helperDetail(id)) {
                is ApiResult.Success -> {
                    val dto = result.data.data
                    if (dto != null) helper = dto.toProfileData()
                    else errorMessage = "Helper not found."
                }
                is ApiResult.Error -> errorMessage = result.message
            }
            isLoading = false
        }
    }

    class Factory(private val helperId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HelperProfileViewModel(helperId, BookingRepository()) as T
        }
    }
}

// ── API model -> screen model ─────────────────────────────────────────────────

private fun HelperDetailDto.toProfileData(): HelperProfileData = HelperProfileData(
    name              = "${fname.orEmpty()} ${lname.orEmpty()}".trim().ifBlank { username.orEmpty() },
    distance          = distance_km?.let { String.format(Locale.US, "%.1f km away", it) } ?: "",
    rating            = avg_rating?.toFloatOrNull() ?: 0f,
    about             = about.orEmpty(),
    services          = services.orEmpty().map {
        ServiceWithPrice(it.name.orEmpty(), formatPrice(it.price_per_hour))
    },
    experience        = formatExperience(years_of_experience),
    languages         = languages_spoken.orEmpty().joinToString(", "),
    availabilityStart = formatTime(start_time),
    availabilityEnd   = formatTime(end_time),
    reviewCount       = rating_count ?: 0,
    workingDays       = working_days.orEmpty().map { it.replaceFirstChar { c -> c.uppercase() } },
    area              = area.orEmpty(),
    city              = city.orEmpty(),
    photoUrl          = absoluteMediaUrl(profile_photo)
)

/** "150.00" -> "₹150/hr" */
private fun formatPrice(raw: String?): String {
    val value = raw?.toDoubleOrNull() ?: return ""
    val text = if (value % 1.0 == 0.0) value.toInt().toString()
    else String.format(Locale.US, "%.2f", value)
    return "₹$text/hr"
}

/** 4 -> "4 years" */
private fun formatExperience(years: Int?): String = when {
    years == null -> ""
    years <= 0    -> "Less than 1 year"
    years == 1    -> "1 year"
    else          -> "$years years"
}

/** "09:00:00" -> "9:00 AM" */
private fun formatTime(raw: String?): String {
    if (raw.isNullOrBlank()) return ""
    return try {
        val input = SimpleDateFormat("HH:mm:ss", Locale.US)
        val output = SimpleDateFormat("h:mm a", Locale.US)
        output.format(input.parse(raw)!!)
    } catch (e: Exception) {
        raw
    }
}

private const val MEDIA_BASE_URL = "http://13.206.80.56"

/** "/media/helper_photos/x.jpg" -> "http://13.206.80.56/media/helper_photos/x.jpg" */
private fun absoluteMediaUrl(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    return MEDIA_BASE_URL + "/" + path.trimStart('/')
}
package com.example.homiee.data.model

import com.google.gson.annotations.SerializedName

// ── GET /api/bookings/helpers/nearby/ ─────────────────────────────────────────

data class NearbyHelpersResponse(
    val status: String?,
    val message: String?,
    val data: List<NearbyHelperDto>?
)

data class NearbyHelperDto(
    val helper_id: Int,
    val fname: String?,
    val lname: String?,
    val username: String?,
    val city: String?,
    val area: String?,
    val profile_photo: String?,
    val years_of_experience: Int?,
    val avg_rating: String?,      // comes as a string, e.g. "4.90"
    val rating_count: Int?,
    val services: List<HelperServiceDto>?,
    val distance_km: Double?
)

// Shared by the nearby list and the helper detail response
data class HelperServiceDto(
    val service_id: Int,
    val name: String?,
    val slug: String?,
    val price_per_hour: String?
)

// ── GET /api/bookings/helpers/{helper_id}/ ────────────────────────────────────

data class HelperDetailResponse(
    val status: String?,
    val message: String?,
    val data: HelperDetailDto?
)

data class HelperDetailDto(
    val helper_id: Int,
    val fname: String?,
    val lname: String?,
    val username: String?,
    val city: String?,
    val area: String?,
    val profile_photo: String?,          // e.g. "/media/helper_photos/2026/07/ramesh.jpg"
    val years_of_experience: Int?,
    val avg_rating: String?,             // string, e.g. "4.90"
    val rating_count: Int?,
    val services: List<HelperServiceDto>?,
    val languages_spoken: List<String>?, // e.g. ["Hindi", "English"]
    val distance_km: Double?,            // null when the resident has no saved location
    val about: String?,
    val working_days: List<String>?,     // lowercase codes: "mon", "tue", ...
    val start_time: String?,             // "09:00:00"
    val end_time: String?,               // "18:00:00"
    val ratings: List<HelperRatingDto>?  // up to 20 recent ratings
)

data class HelperRatingDto(
    val id: Int,
    val booking: Int?,
    val resident: Int?,
    val helper: Int?,
    val score: Int?,
    val feedback: String?,
    val created_at: String?
)

data class CreateBookingRequest(
    @SerializedName("helper_id") val helperId: Int,
    @SerializedName("service_id") val serviceId: Int,
    @SerializedName("booking_date") val bookingDate: String,   // yyyy-MM-dd
    @SerializedName("start_time") val startTime: String,       // HH:mm:ss
    @SerializedName("end_time") val endTime: String,           // HH:mm:ss
    @SerializedName("special_instructions") val specialInstructions: String? = null
)

data class BookingDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("resident_id") val residentId: Int = 0,
    @SerializedName("resident_name") val residentName: String = "",
    @SerializedName("helper_id") val helperId: Int = 0,
    @SerializedName("helper_name") val helperName: String = "",
    @SerializedName("service_id") val serviceId: Int = 0,
    @SerializedName("service") val service: String = "",
    @SerializedName("booking_date") val bookingDate: String = "",
    @SerializedName("start_time") val startTime: String = "",
    @SerializedName("end_time") val endTime: String = "",
    @SerializedName("special_instructions") val specialInstructions: String? = null,
    @SerializedName("total_amount") val totalAmount: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("updated_at") val updatedAt: String = ""
)

// create / detail / cancel
data class BookingResponse(
    val status: String? = null,
    val message: String? = null,
    val data: BookingDto? = null
)

// mine
data class BookingListResponse(
    val status: String? = null,
    val message: String? = null,
    val data: List<BookingDto>? = null
)
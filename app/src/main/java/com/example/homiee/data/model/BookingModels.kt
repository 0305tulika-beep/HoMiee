package com.example.homiee.data.model

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
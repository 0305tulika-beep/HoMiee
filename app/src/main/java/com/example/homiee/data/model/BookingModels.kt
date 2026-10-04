package com.example.homiee.data.model

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

data class HelperServiceDto(
    val service_id: Int,
    val name: String?,
    val slug: String?,
    val price_per_hour: String?
)
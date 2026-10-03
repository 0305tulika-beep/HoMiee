package com.example.homiee.data.model

// ── Address (Step 1) ──
data class ResidentAddressRequest(
    val house_no: String,
    val area: String,
    val city: String,
    val pincode: String,
    val latitude: String,
    val longitude: String
)

data class ResidentAddressResponse(
    val house_no: String,
    val area: String,
    val city: String,
    val pincode: String,
    val latitude: String,
    val longitude: String
)

// ── Photo (Step 3) ──
data class ProfilePhotoResponse(
    val profile_photo: String
)

// ── Full profile (GET) ──
data class ResidentProfileResponse(
    val house_no: String?,
    val area: String?,
    val city: String?,
    val pincode: String?,
    val latitude: String?,
    val longitude: String?,
    val emergency_contact_name: String?,
    val emergency_contact_mobile: String?,
    val profile_photo: String?
)

// ── Generic API error shape (matches Validation error / Unauthorized examples) ──
data class ResidentApiErrorResponse(
    val status: String?,
    val message: String?,
    val errors: Map<String, List<String>>?
)
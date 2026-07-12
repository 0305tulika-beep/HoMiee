package com.example.homiee.data.model

// Confirmed against the live Swagger doc for POST /api/auth/refresh/:
// request  {"refresh": "string"}
// response {"access": "string", "refresh": "string"}  — refresh IS rotated
// on every call, so the new refresh token must always be saved too.
data class RefreshTokenRequest(
    val refresh: String
)

data class RefreshTokenResponse(
    val access: String,
    val refresh: String
)
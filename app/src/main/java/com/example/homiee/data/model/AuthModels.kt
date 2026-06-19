package com.example.homiee.data.model

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val user: UserData,
    val tokens: TokenData
)

data class UserData(
    val id: Int,
    val fname: String,
    val lname: String,
    val email: String,
    val mobile: String,
    val username: String
)

data class TokenData(
    val access: String,
    val refresh: String
)
data class ApiErrorResponse(
    val detail: String? = null,
    val message: String? = null
)

// For parsing 400 validation errors: { "status": "error", "message": "...", "errors": { "field": ["detail"] } }
data class LoginErrorResponse(
    val status: String?,
    val message: String?,
    val errors: Map<String, List<String>>?)

    data class RegisterRequest(
val fname:     String,
val lname:     String,
val email:     String,
val mobile:    String,
val username:  String,
val password:  String,
val password2: String
)

data class RegisterResponse(
    val status:  String,
    val message: String,
    val data:    RegisterData?
)

data class RegisterData(
    val identifier:     String,
    val channel:        String,
    val otp_expires_in: String
)

// ── Verify OTP ────────────────────────────────────────────
data class VerifyOtpRequest(
    val identifier: String,
    val otp:        String
)

data class VerifyOtpResponse(
    val status:  String,
    val message: String,
    val data:    LoginData?   // same shape as login — user + tokens
)

// ── Resend OTP ────────────────────────────────────────────
data class ResendOtpRequest(
    val identifier: String
)

data class ResendOtpResponse(
    val status:  String,
    val message: String,
    val data:    ResendOtpData?
)

data class ResendOtpData(
    val identifier:     String,
    val otp_expires_in: String
)

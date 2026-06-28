package com.example.homiee.data.model

data class LoginRequest(
    val identifier: String,   // UI only collects email now, field name unchanged
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
    val username: String,
    val role: String? = null   // ← NEW: returned on login so app knows which Home to open
)

data class TokenData(
    val access: String,
    val refresh: String
)

data class ApiErrorResponse(
    val detail: String? = null,
    val message: String? = null
)

data class LoginErrorResponse(
    val status: String?,
    val message: String?,
    val errors: Map<String, List<String>>?
)

// ── Register — role REMOVED, no longer collected at signup ──
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

// ── Verify OTP ──
data class VerifyOtpRequest(
    val identifier: String,
    val otp:        String
)

data class VerifyOtpResponse(
    val status:  String,
    val message: String,
    val data:    LoginData?
)

// ── Resend OTP ──
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

// ── Set Role — PLACEHOLDER, update path/shape once backend ships it ──
data class SetRoleRequest(
    val role: String
)

data class SetRoleResponse(
    val status:  String,
    val message: String
)
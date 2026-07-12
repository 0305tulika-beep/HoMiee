package com.example.homiee.data.remote

import com.example.homiee.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/verify-otp/")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("api/auth/resend-otp/")
    suspend fun resendOtp(@Body request: ResendOtpRequest): Response<ResendOtpResponse>

    // ── NEW ──
    @POST("api/auth/logout/")
    suspend fun logout(): Response<LogoutResponse>

    @POST("api/auth/deactivate/")
    suspend fun deactivateAccount(@Body request: DeactivateAccountRequest): Response<DeactivateAccountResponse>


    @HTTP(method = "DELETE", path = "api/auth/delete/", hasBody = true)
    suspend fun deleteAccount(@Body request: DeleteAccountRequest): Response<DeleteAccountResponse>

    // NEW: confirmed live — takes the refresh token, returns a fresh
    // access token (and a rotated refresh token). Used by TokenAuthenticator
    // to silently recover from an expired access token.
    @POST("api/auth/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
}
package com.example.homiee.data.remote

import com.example.homiee.data.model.*
import retrofit2.Response
import retrofit2.http.Body
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

    // ── PLACEHOLDER — update path once backend ships the role-setting endpoint ──
    @POST("api/auth/set-role/")
    suspend fun setRole(@Body request: SetRoleRequest): Response<SetRoleResponse>
}
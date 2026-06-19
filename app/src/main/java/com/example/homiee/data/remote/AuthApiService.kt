package com.example.homiee.data.remote

import com.example.homiee.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/maideasy/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/maideasy/register/")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/maideasy/verify-otp/")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("api/maideasy/resend-otp/")
    suspend fun resendOtp(@Body request: ResendOtpRequest): Response<ResendOtpResponse>
}
package com.example.homiee.data.repository

import com.example.homiee.data.model.*
import com.example.homiee.data.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Response

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

class AuthRepository {

    private val api = RetrofitClient.authApi

    // ── Login ──────────────────────────────────────────────
    suspend fun login(request: LoginRequest): ApiResult<LoginResponse> =
        safeApiCall(isLoginCall = true) { api.login(request) }

    // ── Register ───────────────────────────────────────────
    suspend fun register(request: RegisterRequest): ApiResult<RegisterResponse> =
        safeApiCall { api.register(request) }

    // ── Verify OTP ─────────────────────────────────────────
    suspend fun verifyOtp(request: VerifyOtpRequest): ApiResult<VerifyOtpResponse> =
        safeApiCall { api.verifyOtp(request) }

    // ── Resend OTP ─────────────────────────────────────────
    suspend fun resendOtp(request: ResendOtpRequest): ApiResult<ResendOtpResponse> =
        safeApiCall { api.resendOtp(request) }

    // ── Shared error-handling ──────────────────────────────
    private suspend fun <T> safeApiCall(
        isLoginCall: Boolean = false,
        call: suspend () -> Response<T>
    ): ApiResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.Error("Something went wrong. Please try again.")
            } else {
                val errorBody = response.errorBody()?.string()
                ApiResult.Error(parseErrorMessage(errorBody, response.code(), isLoginCall))
            }
        } catch (e: Exception) {
            ApiResult.Error("Unable to connect. Please check your internet connection.")
        }
    }

    private fun parseErrorMessage(errorBody: String?, code: Int, isLoginCall: Boolean): String {
        if (errorBody == null) return "Something went wrong. Please try again."
        return try {
            val gson = Gson()
            if (isLoginCall) {
                val loginError = gson.fromJson(errorBody, LoginErrorResponse::class.java)
                val firstFieldError = loginError.errors?.values?.firstOrNull()?.firstOrNull()
                if (firstFieldError != null) return toFriendlyMessage(firstFieldError, code)
                loginError.message?.let { return toFriendlyMessage(it, code) }
            }
            val parsed = gson.fromJson(errorBody, ApiErrorResponse::class.java)
            val raw = parsed.detail ?: parsed.message ?: return genericMessageFor(code)
            toFriendlyMessage(raw, code)
        } catch (e: Exception) {
            genericMessageFor(code)
        }
    }

    private fun toFriendlyMessage(raw: String, code: Int): String = when {
        raw.contains("identifier", ignoreCase = true) ->
            "Please check your username, email, or mobile number."
        raw.contains("password", ignoreCase = true) && code == 400 ->
            "Incorrect password. Please try again."
        else -> raw
    }

    private fun genericMessageFor(code: Int): String = when (code) {
        400 -> "Please check your details and try again."
        401 -> "Incorrect username or password."
        409 -> "Email, mobile, or username is already taken."
        429 -> "Too many attempts. Please try again later."
        else -> "Something went wrong. Please try again."
    }
}
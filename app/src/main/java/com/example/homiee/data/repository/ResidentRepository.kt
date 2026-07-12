package com.example.homiee.data.repository

import com.example.homiee.data.model.*
import com.example.homiee.data.remote.RetrofitClient
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Response

class ResidentRepository {

    private val api = RetrofitClient.residentApi

    suspend fun saveAddress(request: ResidentAddressRequest): ApiResult<ResidentAddressResponse> =
        safeApiCall { api.saveAddress(request) }

    suspend fun uploadPhoto(part: MultipartBody.Part): ApiResult<ProfilePhotoResponse> =
        safeApiCall { api.uploadPhoto(part) }

    suspend fun getProfile(): ApiResult<ResidentProfileResponse> =
        safeApiCall { api.getProfile() }

    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.Error("Something went wrong. Please try again.")
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("RESIDENT_API_ERROR", "HTTP ${response.code()} - Body: $errorBody")
                ApiResult.Error(parseErrorMessage(errorBody, response.code()))
            }
        } catch (e: Exception) {
            android.util.Log.e("RESIDENT_API_ERROR", "Exception: ${e.javaClass.simpleName} - ${e.message}", e)
            ApiResult.Error("Unable to connect. Please check your internet connection.")
        }
    }

    private fun parseErrorMessage(errorBody: String?, code: Int): String {
        if (errorBody == null) return genericMessageFor(code)
        return try {
            val parsed = Gson().fromJson(errorBody, ResidentApiErrorResponse::class.java)
            val firstFieldError = parsed.errors?.values?.firstOrNull()?.firstOrNull()
            firstFieldError ?: parsed.message ?: genericMessageFor(code)
        } catch (e: Exception) {
            genericMessageFor(code)
        }
    }

    private fun genericMessageFor(code: Int): String = when (code) {
        400 -> "Please check your details and try again."
        401 -> "Your session has expired. Please log in again."
        else -> "Something went wrong. Please try again."
    }
}
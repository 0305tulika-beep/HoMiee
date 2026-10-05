package com.example.homiee.data.repository

import com.example.homiee.data.model.*
import com.example.homiee.data.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Response

class BookingRepository {

    private val api = RetrofitClient.bookingApi

    suspend fun nearbyHelpers(radiusKm: Double? = null, service: String? = null): ApiResult<NearbyHelpersResponse> =
        safeApiCall { api.nearbyHelpers(radiusKm, service) }

    suspend fun helperDetail(helperId: Int): ApiResult<HelperDetailResponse> =
        safeApiCall { api.helperDetail(helperId) }

    suspend fun helpersByCategory(service: String, radiusKm: Double? = null): ApiResult<NearbyHelpersResponse> =
        safeApiCall { api.helpersByCategory(service, radiusKm) }

    suspend fun search(q: String? =null): ApiResult<NearbyHelpersResponse> =
        safeApiCall { api.search(q)}

    suspend fun createBooking(request: CreateBookingRequest): ApiResult<BookingResponse> =
        safeApiCall { api.createBooking(request) }

    suspend fun myBookings(status: String? = null): ApiResult<BookingListResponse> =
        safeApiCall { api.myBookings(status) }

    suspend fun bookingDetail(id: Int): ApiResult<BookingResponse> =
        safeApiCall { api.bookingDetail(id) }

    suspend fun cancelBooking(id: Int): ApiResult<BookingResponse> =
        safeApiCall { api.cancelBooking(id) }

    private suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.Error("Something went wrong. Please try again.")
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("BOOKING_API_ERROR", "HTTP ${response.code()} - Body: ${errorBody?.take(300)}")
                ApiResult.Error(parseErrorMessage(errorBody, response.code()))
            }
        } catch (e: Exception) {
            android.util.Log.e("BOOKING_API_ERROR", "Exception: ${e.javaClass.simpleName} - ${e.message}", e)
            ApiResult.Error("Unable to connect. Please check your internet connection.")
        }
    }

    private fun parseErrorMessage(errorBody: String?, code: Int): String {
        if (errorBody == null) return genericMessageFor(code)
        return try {
            val parsed = Gson().fromJson(errorBody, ResidentApiErrorResponse::class.java)
            parsed.errors?.values?.firstOrNull()?.firstOrNull()
                ?: parsed.message
                ?: genericMessageFor(code)
        } catch (e: Exception) {
            genericMessageFor(code)
        }
    }

    private fun genericMessageFor(code: Int): String = when (code) {
        400 -> "Please check your details and try again."
        401 -> "Your session has expired. Please log in again."
        403 -> "You don't have access to this."
        else -> "Something went wrong. Please try again."
    }
}
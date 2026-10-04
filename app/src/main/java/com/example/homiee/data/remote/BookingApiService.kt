package com.example.homiee.data.remote

import com.example.homiee.data.model.HelperDetailResponse
import com.example.homiee.data.model.NearbyHelpersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingApiService {

    // Both params are optional. Retrofit leaves out any query param that is null.
    @GET("api/bookings/helpers/nearby/")
    suspend fun nearbyHelpers(
        @Query("radius_km") radiusKm: Double? = null,
        @Query("service") service: String? = null
    ): Response<NearbyHelpersResponse>

    // helper id ki api
    @GET("api/bookings/helpers/{helper_id}/")
    suspend fun helperDetail(@Path("helper_id") helperId: Int): Response<HelperDetailResponse>
}
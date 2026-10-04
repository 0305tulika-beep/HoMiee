package com.example.homiee.data.remote

import com.example.homiee.data.model.NearbyHelpersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookingApiService {

    // Both params are optional. Retrofit leaves out any query param that is null.
    @GET("api/bookings/helpers/nearby/")
    suspend fun nearbyHelpers(
        @Query("radius_km") radiusKm: Double? = null,
        @Query("service") service: String? = null
    ): Response<NearbyHelpersResponse>
}
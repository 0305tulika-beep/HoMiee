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

    //search me profiles ki api
    @GET("api/bookings/helpers/category/")
    suspend fun helpersByCategory(
        @Query("service") service: String,
        @Query("radius_km") radiusKm: Double? = null
    ): Response<NearbyHelpersResponse>

    //search ki api
    @GET("api/bookings/helpers/search/")
    suspend fun search(
        @Query("q") q: String? =null,
    ): Response<NearbyHelpersResponse>


}
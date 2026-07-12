package com.example.homiee.data.remote

import com.example.homiee.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ResidentApiService {

    @POST("api/userdetails/residents/address/")
    suspend fun saveAddress(@Body request: ResidentAddressRequest): Response<ResidentAddressResponse>

    @Multipart
    @POST("api/userdetails/residents/photo/")
    suspend fun uploadPhoto(@Part profile_photo: MultipartBody.Part): Response<ProfilePhotoResponse>

    @GET("api/userdetails/residents/profile/")
    suspend fun getProfile(): Response<ResidentProfileResponse>
}
package com.example.homiee.data.remote

import android.content.Context
import com.example.homiee.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "http://13.206.80.56/"

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val NO_AUTH_PATHS = setOf(
        "/api/auth/login/",
        "/api/auth/register/",
        "/api/auth/verify-otp/",
        "/api/auth/resend-otp/",
        "/api/auth/find-account/",
        "/api/auth/password/reset/request/",
        "/api/auth/password/reset/verify-otp/",
        "/api/auth/password/reset/confirm/",
        "/api/auth/auth/google/",
        "/api/auth/refresh/"
    )

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = SessionManager.accessToken
        val isPublicPath = original.url.encodedPath in NO_AUTH_PATHS
        val request = if (!token.isNullOrBlank() && !isPublicPath) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }
        chain.proceed(request)
    }

    // CHANGED: val -> by lazy
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(TokenAuthenticator(appContext))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // CHANGED: val -> by lazy
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // CHANGED: val -> by lazy
    val authApi: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
    val residentApi: ResidentApiService by lazy { retrofit.create(ResidentApiService::class.java) }
    val bookingApi: BookingApiService by lazy { retrofit.create(BookingApiService::class.java) }

    private val refreshOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // CHANGED: val -> by lazy (baseUrl is fine, but keep it lazy for consistency/safety)
    private val refreshRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val refreshAuthApi: AuthApiService by lazy { refreshRetrofit.create(AuthApiService::class.java) }
}
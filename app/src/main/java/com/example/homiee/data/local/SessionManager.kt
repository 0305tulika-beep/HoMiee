package com.example.homiee.data.local

/**
 * In-memory holder for the current access token, so OkHttp interceptors
 * (which run synchronously) can read it without needing a suspend call
 * into DataStore on every single request.
 *
 * Populate this:
 *  1. On app start (e.g. in your Application class or Splash screen) by
 *     reading TokenManager.getAccessToken() once.
 *  2. Right after a successful login / verify-otp, alongside tokenManager.saveTokens(...)
 */
object SessionManager {
    @Volatile
    var accessToken: String? = null

    fun clear() {
        accessToken = null
    }
}
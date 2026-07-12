package com.example.homiee.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    // NEW: flips to true whenever TokenAuthenticator sees a 401 it can't
    // recover from (no refresh-token endpoint exists yet — see
    // TokenAuthenticator). Observed at the nav-graph level to redirect the
    // user back to login instead of leaving them stuck on a screen that
    // silently keeps failing every request.
    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired: StateFlow<Boolean> = _sessionExpired.asStateFlow()

    fun clear() {
        accessToken = null
    }

    /** Called by TokenAuthenticator when a request 401s and can't be recovered. */
    fun notifySessionExpired() {
        accessToken = null
        _sessionExpired.value = true
    }

    /** Called once the UI has reacted (navigated to login) so it doesn't fire again. */
    fun consumeSessionExpired() {
        _sessionExpired.value = false
    }
}
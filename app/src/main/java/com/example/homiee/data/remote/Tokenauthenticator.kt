package com.example.homiee.data.remote

import android.content.Context
import com.example.homiee.data.local.SessionManager
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.model.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Fires automatically whenever any request on the main OkHttpClient comes
 * back 401 (e.g. "Token is expired").
 *
 * Flow:
 *  1. Ignore 401s from requests that never carried an Authorization header
 *     in the first place (wrong password on login, wrong OTP, etc.) — those
 *     are normal validation failures, not a session expiry, and are already
 *     handled by the caller's existing error-message UI.
 *  2. Ignore if we've already tried once for this request chain (avoids an
 *     infinite retry loop if the new access token is somehow rejected too).
 *  3. Read the stored refresh token and call POST api/auth/refresh/
 *     (via RetrofitClient.refreshAuthApi — a client with no auth header and
 *     no authenticator attached, to avoid re-entering this same flow).
 *  4. On success: persist the new access + refresh tokens (this endpoint
 *     rotates both), update SessionManager's in-memory copy, and retry the
 *     original request with the new access token.
 *  5. On failure (refresh token itself invalid/expired, or the call fails):
 *     clear all stored auth state and signal the app (via
 *     SessionManager.sessionExpired) to route back to login.
 */
class TokenAuthenticator(private val appContext: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val hadAuthHeader = response.request.header("Authorization") != null
        if (!hadAuthHeader) {
            // Not an authenticated request — this 401 is a normal
            // validation failure, not a session expiry.
            return null
        }
        if (responseCount(response) >= 2) {
            // Already retried once for this chain and still failing — give up.
            return null
        }

        val tokenManager = TokenManager(appContext)
        val refreshToken = runBlocking { tokenManager.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) {
            runBlocking { tokenManager.clearTokens() }
            SessionManager.notifySessionExpired()
            return null
        }

        val refreshResponse = try {
            runBlocking { RetrofitClient.refreshAuthApi.refreshToken(RefreshTokenRequest(refreshToken)) }
        } catch (e: Exception) {
            null
        }

        val newTokens = refreshResponse?.takeIf { it.isSuccessful }?.body()
        if (newTokens == null) {
            // Refresh token is also invalid/expired (or the call failed) —
            // nothing more we can do automatically.
            runBlocking { tokenManager.clearTokens() }
            SessionManager.notifySessionExpired()
            return null
        }

        // This endpoint rotates both tokens — always save both.
        runBlocking { tokenManager.saveTokens(newTokens.access, newTokens.refresh) }
        SessionManager.accessToken = newTokens.access

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.access}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}
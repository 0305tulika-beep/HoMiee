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

// One lock for the whole app: only ONE refresh may run at a time.
private val REFRESH_LOCK = Any()

class TokenAuthenticator(private val appContext: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val sentAuth = response.request.header("Authorization")
            ?: return null
        if (responseCount(response) >= 2) return null

        synchronized(REFRESH_LOCK) {
            // Another request may have refreshed while we were waiting for the lock.
            val currentAccess = SessionManager.accessToken
            if (!currentAccess.isNullOrBlank() && "Bearer $currentAccess" != sentAuth) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccess")
                    .build()
            }

            val tokenManager = TokenManager(appContext)
            val refreshToken = runBlocking { tokenManager.getRefreshToken() }
            if (refreshToken.isNullOrBlank()) {
                expireSession(tokenManager)
                return null
            }

            val refreshResponse = try {
                runBlocking { RetrofitClient.refreshAuthApi.refreshToken(RefreshTokenRequest(refreshToken)) }
            } catch (e: Exception) {
                // No internet / timeout: this is NOT a reason to log the user out.
                return null
            }

            if (refreshResponse.isSuccessful) {
                val newTokens = refreshResponse.body() ?: return null

                // This endpoint rotates both tokens - always save both.
                runBlocking { tokenManager.saveTokens(newTokens.access, newTokens.refresh) }
                SessionManager.accessToken = newTokens.access

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.access}")
                    .build()
            }

            // The server answered: only a rejection of the refresh token ends the session.
            val code = refreshResponse.code()
            if (code == 400 || code == 401 || code == 403) {
                expireSession(tokenManager)
            }
            return null
        }
    }

    /** Session is really over: remove ONLY the tokens, keep the user's details. */
    private fun expireSession(tokenManager: TokenManager) {
        runBlocking { tokenManager.clearSessionTokens() }
        SessionManager.notifySessionExpired()
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
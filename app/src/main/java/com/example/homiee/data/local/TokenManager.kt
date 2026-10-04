package com.example.homiee.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "homiee_auth_prefs")

class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN_KEY   = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY  = stringPreferencesKey("refresh_token")
    private val FORMS_COMPLETE_KEY = booleanPreferencesKey("forms_completed")
    private val CURRENT_STEP_KEY   = stringPreferencesKey("current_form_step")
    private val FIRST_NAME_KEY     = stringPreferencesKey("first_name")
    private val LAST_NAME_KEY      = stringPreferencesKey("last_name")
    private val EMAIL_KEY          = stringPreferencesKey("email")

    // ── Tokens ────────────────────────────────────────────────────────────────

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = access
            prefs[REFRESH_TOKEN_KEY] = refresh
        }
    }

    /** For refresh responses that return only a new access token (rotation off). */
    suspend fun saveAccessToken(access: String) {
        context.dataStore.edit { prefs -> prefs[ACCESS_TOKEN_KEY] = access }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    /**
     * True while the user still holds a session. An expired ACCESS token does not
     * mean logged out - the refresh token can get a new one - so check both.
     */
    suspend fun isLoggedIn(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[ACCESS_TOKEN_KEY] != null || prefs[REFRESH_TOKEN_KEY] != null
    }

    // ── Clearing ──────────────────────────────────────────────────────────────

    /**
     * Removes ONLY the access + refresh tokens. The user's name, email and
     * "forms completed" flag stay, so after logging back in nothing is lost.
     * Use this when a session dies on its own (refresh token rejected).
     */
    suspend fun clearSessionTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }

    /**
     * Wipes EVERYTHING (tokens, name, email, form progress).
     * Use ONLY for a manual logout or account deletion.
     */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }

    @Deprecated(
        "Wipes everything. Use clearSessionTokens() for an expired session, " +
                "or clearAll() for a manual logout / account deletion.",
        ReplaceWith("clearAll()")
    )
    suspend fun clearTokens() = clearAll()

    // ── Forms / onboarding ────────────────────────────────────────────────────

    suspend fun saveCurrentStep(route: String) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_STEP_KEY] = route
        }
    }

    suspend fun getCurrentStep(): String? {
        return context.dataStore.data.first()[CURRENT_STEP_KEY]
    }

    suspend fun markFormsCompleted() {
        context.dataStore.edit { prefs ->
            prefs[FORMS_COMPLETE_KEY] = true
        }
    }

    suspend fun areFormsCompleted(): Boolean {
        return context.dataStore.data.first()[FORMS_COMPLETE_KEY] ?: false
    }

    // ── User details ──────────────────────────────────────────────────────────

    suspend fun saveUserName(firstName: String, lastName: String) {
        context.dataStore.edit { prefs ->
            prefs[FIRST_NAME_KEY] = firstName
            prefs[LAST_NAME_KEY]  = lastName
        }
    }

    suspend fun getFirstName(): String? {
        return context.dataStore.data.first()[FIRST_NAME_KEY]
    }

    suspend fun getLastName(): String? {
        return context.dataStore.data.first()[LAST_NAME_KEY]
    }

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { prefs -> prefs[EMAIL_KEY] = email }
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.first()[EMAIL_KEY]
    }
}
package com.example.homiee.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "homiee_auth_prefs")

class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val ROLE_KEY = stringPreferencesKey("user_role")   // ← NEW

    suspend fun saveTokens(access: String, refresh: String, role: String) {  // ← role param added
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = access
            prefs[REFRESH_TOKEN_KEY] = refresh
            prefs[ROLE_KEY] = role                              // ← NEW
        }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    suspend fun getRole(): String? {                            // ← NEW
        return context.dataStore.data.first()[ROLE_KEY]
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}
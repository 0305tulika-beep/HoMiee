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

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = access
            prefs[REFRESH_TOKEN_KEY] = refresh
        }
    }

    suspend fun saveCurrentStep(route: String) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_STEP_KEY] = route
        }
    }

    suspend fun markFormsCompleted() {
        context.dataStore.edit { prefs ->
            prefs[FORMS_COMPLETE_KEY] = true
        }
    }

    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    suspend fun getCurrentStep(): String? {
        return context.dataStore.data.first()[CURRENT_STEP_KEY]
    }

    suspend fun areFormsCompleted(): Boolean {
        return context.dataStore.data.first()[FORMS_COMPLETE_KEY] ?: false
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }

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

    private val EMAIL_KEY = stringPreferencesKey("email")

    suspend fun saveEmail(email: String) {
        context.dataStore.edit { prefs -> prefs[EMAIL_KEY] = email }
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.first()[EMAIL_KEY]
    }
}
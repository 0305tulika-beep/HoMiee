package com.example.homiee.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.bookingDataStore by preferencesDataStore(name = "homiee_booking_prefs")

class BookingPrefsManager(private val context: Context) {

    private val PENDING_CONFIRMATION_KEY = booleanPreferencesKey("show_booking_confirmation")
    private val CONFIRMED_BOOKING_ID_KEY = stringPreferencesKey("confirmed_booking_id")

    suspend fun markBookingConfirmed(bookingId: String) {
        context.bookingDataStore.edit { prefs ->
            prefs[PENDING_CONFIRMATION_KEY] = true
            prefs[CONFIRMED_BOOKING_ID_KEY] = bookingId
        }
    }

    suspend fun hasUnseenConfirmation(): Boolean {
        return context.bookingDataStore.data.first()[PENDING_CONFIRMATION_KEY] ?: false
    }

    suspend fun getConfirmedBookingId(): String? {
        return context.bookingDataStore.data.first()[CONFIRMED_BOOKING_ID_KEY]
    }

    suspend fun clearConfirmation() {
        context.bookingDataStore.edit { prefs ->
            prefs[PENDING_CONFIRMATION_KEY] = false
        }
    }
}
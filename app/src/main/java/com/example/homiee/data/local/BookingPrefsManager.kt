package com.example.homiee.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.bookingDataStore by preferencesDataStore(name = "homiee_booking_prefs")

/**
 * Remembers which bookings the resident has made that are still waiting for a helper to accept.
 * When the server later reports one of them as "confirmed", the confirmation screen is shown once
 * and the id is removed (see BookingViewModel.refresh / dismissConfirmation).
 */
class BookingPrefsManager(private val context: Context) {

    private val AWAITING_KEY = stringSetPreferencesKey("awaiting_acceptance_ids")

    suspend fun getAwaitingIds(): Set<String> =
        context.bookingDataStore.data.first()[AWAITING_KEY] ?: emptySet()

    suspend fun addAwaiting(ids: Collection<String>) {
        if (ids.isEmpty()) return
        context.bookingDataStore.edit { prefs ->
            prefs[AWAITING_KEY] = (prefs[AWAITING_KEY] ?: emptySet()) + ids
        }
    }

    suspend fun removeAwaiting(id: String) {
        context.bookingDataStore.edit { prefs ->
            prefs[AWAITING_KEY] = (prefs[AWAITING_KEY] ?: emptySet()) - id
        }
    }

    suspend fun removeAwaiting(ids: Collection<String>) {
        if (ids.isEmpty()) return
        context.bookingDataStore.edit { prefs ->
            prefs[AWAITING_KEY] = (prefs[AWAITING_KEY] ?: emptySet()) - ids.toSet()
        }
    }

    /** Call on logout so the next account doesn't inherit this list. */
    suspend fun clearAll() {
        context.bookingDataStore.edit { it.clear() }
    }
}
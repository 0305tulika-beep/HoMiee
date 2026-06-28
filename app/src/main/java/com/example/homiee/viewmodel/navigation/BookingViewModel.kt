package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.BookingPrefsManager
import com.example.homiee.ui.screens.Residentflow.BookingItem
import com.example.homiee.ui.screens.Residentflow.BookingTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingPrefsManager: BookingPrefsManager
) : ViewModel() {

    private val _bookings = MutableStateFlow(
        mutableListOf(
            BookingItem("b001", "Ramesh Kumar", "Cleaning", 4.9f, BookingTab.ACTIVE),
            BookingItem("b002", "Ramesh Kumar", "Cleaning", 4.9f, BookingTab.ACTIVE),
            BookingItem("b004", "Kavita Singh", "Laundry", 4.6f, BookingTab.UPCOMING,
                isPending = false, bookingDate = "Jun 16, 2026", bookingTime = "2:00 PM"),
            BookingItem("b005", "Meena Verma", "Laundry", 4.7f, BookingTab.COMPLETED)
        )
    )
    val bookings: StateFlow<List<BookingItem>> = _bookings.asStateFlow()

    private val _lastCreatedBooking = MutableStateFlow<BookingItem?>(null)
    val lastCreatedBooking: StateFlow<BookingItem?> = _lastCreatedBooking.asStateFlow()

    private val _showConfirmation = MutableStateFlow(false)
    val showConfirmation: StateFlow<Boolean> = _showConfirmation.asStateFlow()

    fun createBooking(
        helperName: String,
        service: String,
        rating: Float,
        date: String,
        time: String
    ): String {
        val newId = "b${System.currentTimeMillis()}"
        val newBooking = BookingItem(
            id          = newId,
            helperName  = helperName,
            service     = service,
            rating      = rating,
            status      = BookingTab.UPCOMING,
            isPending   = true,
            bookingDate = date,
            bookingTime = time
        )
        _bookings.value = (_bookings.value + newBooking).toMutableList()
        _lastCreatedBooking.value = newBooking

        // Simulate the helper accepting after a short delay → moves Pending to a real Upcoming card,
        // and persists a flag so the Confirmation screen shows even after app restart.
        viewModelScope.launch {
            kotlinx.coroutines.delay(4000)
            confirmBooking(newId)
            bookingPrefsManager.markBookingConfirmed(newId)
        }

        return newId
    }

    private fun confirmBooking(bookingId: String) {
        _bookings.value = _bookings.value.map {
            if (it.id == bookingId) it.copy(isPending = false) else it
        }.toMutableList()
    }

    fun getBookingById(id: String): BookingItem? =
        _bookings.value.find { it.id == id }

    // ── Called by Splash or Home to check if a confirmation popup is owed ──
    fun checkPendingConfirmation() {
        viewModelScope.launch {
            if (bookingPrefsManager.hasUnseenConfirmation()) {
                val confirmedId = bookingPrefsManager.getConfirmedBookingId()
                if (confirmedId != null && getBookingById(confirmedId) == null) {
                    // App was killed before the in-memory booking existed —
                    // recreate a minimal confirmed booking so Details has something to show.
                    _bookings.value = (_bookings.value + BookingItem(
                        id = confirmedId,
                        helperName = "Ramesh Kumar",
                        service = "Cleaning",
                        rating = 4.9f,
                        status = BookingTab.UPCOMING,
                        isPending = false,
                        bookingDate = "Jun 12, 2026",
                        bookingTime = "10:00 AM"
                    )).toMutableList()
                }
                _lastCreatedBooking.value = confirmedId?.let { getBookingById(it) }
                _showConfirmation.value = true
            }
        }
    }

    fun dismissConfirmation() {
        _showConfirmation.value = false
        viewModelScope.launch {
            bookingPrefsManager.clearConfirmation()
        }
    }
}

class BookingViewModelFactory(
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BookingViewModel(BookingPrefsManager(context.applicationContext)) as T
    }
}
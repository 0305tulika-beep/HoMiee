package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.BookingPrefsManager
import com.example.homiee.data.model.BookingDto
import com.example.homiee.data.model.CreateBookingRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import com.example.homiee.ui.screens.Residentflow.BookingItem
import com.example.homiee.ui.screens.Residentflow.tabAt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class BookingDetailState(
    val isLoading: Boolean = true,
    val booking: BookingItem? = null,
    val errorMessage: String? = null
)

private const val STATUS_CONFIRMED = "confirmed"
private const val STATUS_COMPLETED = "completed"

// What the backend sends when a helper declines. CHANGE THESE to match your Django status values.
private val DECLINED_STATUSES = setOf("rejected", "declined")

private fun BookingItem.isDeclined(): Boolean = rawStatus.lowercase() in DECLINED_STATUSES

/** "2026-07-12" + "10:30:00" -> epoch millis in the phone's time zone (0 if it can't be parsed). */
private fun parseBookingMillis(date: String, time: String): Long = try {
    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse("$date $time")?.time ?: 0L
} catch (e: Exception) {
    0L
}

/**
 * Applies the booking's real start/end moments to a mapped item, so the screens can move a
 * confirmed booking Upcoming -> Active -> Completed as the clock passes (see BookingItem.tabAt).
 */
private fun BookingItem.withTiming(dto: BookingDto): BookingItem {
    val timed = copy(
        rawStatus = dto.status,
        startMillis = parseBookingMillis(dto.bookingDate, dto.startTime),
        endMillis = parseBookingMillis(dto.bookingDate, dto.endTime)
    )
    return timed.copy(status = timed.tabAt(System.currentTimeMillis()))
}

private fun BookingDto.toTimedItemOrNull(): BookingItem? = toBookingItemOrNull()?.withTiming(this)

private fun BookingDto.toTimedItem(): BookingItem = toBookingItem().withTiming(this)

class BookingViewModel(
    private val bookingPrefsManager: BookingPrefsManager,
    private val repo: BookingRepository = BookingRepository()
) : ViewModel() {

    // ── My bookings (list) ───────────────────────────────────────────────────
    private val _bookings = MutableStateFlow<List<BookingItem>>(emptyList())
    val bookings: StateFlow<List<BookingItem>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow<String?>(null)
    val loadError: StateFlow<String?> = _loadError.asStateFlow()

    private var refreshJob: Job? = null

    // ── Single booking (details) ─────────────────────────────────────────────
    private val _detail = MutableStateFlow(BookingDetailState())
    val detail: StateFlow<BookingDetailState> = _detail.asStateFlow()

    private var detailJob: Job? = null

    // ── Create / cancel / rate ───────────────────────────────────────────────
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _submitError = MutableStateFlow<String?>(null)
    val submitError: StateFlow<String?> = _submitError.asStateFlow()

    private val _isCancelling = MutableStateFlow(false)
    val isCancelling: StateFlow<Boolean> = _isCancelling.asStateFlow()

    private val _isRating = MutableStateFlow(false)
    val isRating: StateFlow<Boolean> = _isRating.asStateFlow()

    /** One-shot messages (shown as toasts by the screens). */
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    // ── Confirmation popup ───────────────────────────────────────────────────
    // Set when a booking the resident was waiting on has been accepted by the helper.
    private val _lastCreatedBooking = MutableStateFlow<BookingItem?>(null)
    val lastCreatedBooking: StateFlow<BookingItem?> = _lastCreatedBooking.asStateFlow()

    private val _showConfirmation = MutableStateFlow(false)
    val showConfirmation: StateFlow<Boolean> = _showConfirmation.asStateFlow()

    // ── Declined popup ───────────────────────────────────────────────────────
    // Set when a booking the resident was waiting on has been declined by the helper.
    private val _declinedBooking = MutableStateFlow<BookingItem?>(null)
    val declinedBooking: StateFlow<BookingItem?> = _declinedBooking.asStateFlow()

    private val _showDeclined = MutableStateFlow(false)
    val showDeclined: StateFlow<Boolean> = _showDeclined.asStateFlow()

    // The create endpoint has no address field, so the GPS address is only kept on the device.
    private val localAddresses = mutableMapOf<String, String>()

    // Bookings we've already asked the server to mark completed (so we don't repeat the call).
    private val completeAttempted = mutableSetOf<String>()

    private fun BookingItem.withLocalAddress(): BookingItem =
        localAddresses[id]?.let { copy(address = it) } ?: this

    // NOTE: no fetch in init. This ViewModel is created before login,
    // so screens call refresh() when they appear.
    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            _isLoading.value = true
            _loadError.value = null

            when (val result = repo.myBookings(null)) {
                is ApiResult.Success -> {
                    val raw = result.data.data.orEmpty()
                    val fresh = raw
                        .mapNotNull { it.toTimedItemOrNull() }
                        .map { it.withLocalAddress() }
                    // The mapper skips rejected bookings (they don't belong in any tab), but we
                    // still need them here to detect a decline. They are NOT added to the list.
                    val declinedItems = raw
                        .filter { it.status.lowercase() in DECLINED_STATUSES }
                        .map { it.toTimedItem().withLocalAddress() }
                    _bookings.value = fresh

                    android.util.Log.d(
                        "BOOKING_POPUP",
                        "server returned ${raw.size}, mapped ${fresh.size}, " +
                                "server statuses=${raw.map { "${it.id}:${it.status}" }}"
                    )

                    detectAcceptedBooking(fresh, declinedItems)
                    completeFinishedBookings(fresh)
                }
                is ApiResult.Error -> _loadError.value = result.message
            }
            _isLoading.value = false
        }
    }

    /**
     * Compares the fresh list against the persisted "awaiting acceptance" ids.
     *  - a booking that is pending now is remembered as awaiting
     *  - an awaiting booking that the server now reports as confirmed = helper accepted it -> popup
     *  - an awaiting booking that is gone, cancelled, rejected or already completed is forgotten
     * Because the ids live in DataStore, this works even if the app was closed when the helper accepted.
     */
    private suspend fun detectAcceptedBooking(
        fresh: List<BookingItem>,
        declinedItems: List<BookingItem>
    ) {
        val awaiting = bookingPrefsManager.getAwaitingIds()
        android.util.Log.d(
            "BOOKING_POPUP",
            "awaiting=$awaiting, app view=${fresh.map { "${it.id}:${it.rawStatus}:pending=${it.isPending}" }}"
        )

        val pendingIds = fresh.filter { it.isPending }.map { it.id }
        bookingPrefsManager.addAwaiting(pendingIds)

        val byId = (fresh + declinedItems).associateBy { it.id }
        val stale = awaiting.filter { id ->
            val b = byId[id]
            b == null || (!b.isPending &&
                    !b.rawStatus.equals(STATUS_CONFIRMED, true) &&
                    !b.isDeclined())
        }
        bookingPrefsManager.removeAwaiting(stale)

        val accepted = fresh.firstOrNull {
            it.id in awaiting && it.rawStatus.equals(STATUS_CONFIRMED, true)
        }
        // Only one popup at a time; accepted takes priority, a declined one shows on a later refresh
        if (_showConfirmation.value || _showDeclined.value) return

        if (accepted != null) {
            _lastCreatedBooking.value = accepted
            _showConfirmation.value = true
            return
        }

        val declined = declinedItems.firstOrNull { it.id in awaiting }
        if (declined != null) {
            _declinedBooking.value = declined
            _showDeclined.value = true
        }
    }

    /**
     * Confirmed bookings whose end time has passed are marked completed on the server too, so the
     * helper's app and the rating API (which needs a completed booking) agree with the Completed tab.
     */
    private fun completeFinishedBookings(list: List<BookingItem>) {
        val now = System.currentTimeMillis()
        list.filter {
            it.rawStatus.equals(STATUS_CONFIRMED, true) && it.endMillis in 1..now
        }.forEach { item ->
            if (completeAttempted.add(item.id)) {
                viewModelScope.launch {
                    if (!markCompleted(item.id)) completeAttempted.remove(item.id)   // allow a retry next refresh
                }
            }
        }
    }

    /** Calls the complete endpoint and, on success, flips the local copy to completed. */
    private suspend fun markCompleted(id: String): Boolean {
        val intId = id.toIntOrNull() ?: return false
        return when (repo.completeBooking(intId)) {
            is ApiResult.Success -> {
                _bookings.value = _bookings.value.map {
                    if (it.id == id) it.copy(rawStatus = STATUS_COMPLETED) else it
                }
                true
            }
            is ApiResult.Error -> false
        }
    }

    fun loadDetail(id: String) {
        val cached = getBookingById(id)
        _detail.value = BookingDetailState(isLoading = cached == null, booking = cached)

        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            val intId = id.toIntOrNull()
            if (intId == null) {
                _detail.value = BookingDetailState(isLoading = false, errorMessage = "Invalid booking.")
                return@launch
            }
            when (val result = repo.bookingDetail(intId)) {
                is ApiResult.Success -> {
                    val item = result.data.data?.toTimedItem()?.withLocalAddress()
                    _detail.value = BookingDetailState(
                        isLoading = false,
                        booking = item ?: cached,
                        errorMessage = if (item == null && cached == null)
                            "Something went wrong. Please try again." else null
                    )
                }
                is ApiResult.Error -> _detail.value = BookingDetailState(
                    isLoading = false,
                    booking = cached,                       // keep showing cached data if we have it
                    errorMessage = if (cached == null) result.message else null
                )
            }
        }
    }

    fun createBooking(
        helperId: Int,
        serviceId: Int,
        bookingDate: String,          // yyyy-MM-dd
        startTime: String,            // HH:mm:ss
        endTime: String,              // HH:mm:ss
        specialInstructions: String,
        address: String,
        onSuccess: (bookingId: String) -> Unit
    ) {
        if (_isSubmitting.value) return
        viewModelScope.launch {
            _isSubmitting.value = true
            _submitError.value = null

            val request = CreateBookingRequest(
                helperId = helperId,
                serviceId = serviceId,
                bookingDate = bookingDate,
                startTime = startTime,
                endTime = endTime,
                specialInstructions = specialInstructions.trim().ifBlank { null }
            )

            when (val result = repo.createBooking(request)) {
                is ApiResult.Success -> {
                    val dto = result.data.data
                    if (dto == null) {
                        _submitError.value = "Something went wrong. Please try again."
                    } else {
                        val item = dto.toTimedItem()
                        if (address.isNotBlank()) localAddresses[item.id] = address
                        val withAddress = item.withLocalAddress()
                        _bookings.value = _bookings.value.filterNot { it.id == withAddress.id } + withAddress

                        // Remember it as "waiting for the helper" so we can pop the confirmation
                        // later, even if the app is closed when the helper accepts.
                        bookingPrefsManager.addAwaiting(listOf(withAddress.id))

                        onSuccess(withAddress.id)
                    }
                }
                is ApiResult.Error -> _submitError.value = result.message
            }
            _isSubmitting.value = false
        }
    }

    fun clearSubmitError() { _submitError.value = null }

    fun cancelBooking(id: String, onResult: (success: Boolean) -> Unit = {}) {
        val intId = id.toIntOrNull() ?: return
        if (_isCancelling.value) return
        viewModelScope.launch {
            _isCancelling.value = true
            when (val result = repo.cancelBooking(intId)) {
                is ApiResult.Success -> {
                    _bookings.value = _bookings.value.filterNot { it.id == id }
                    bookingPrefsManager.removeAwaiting(id)
                    _messages.tryEmit("Booking cancelled")
                    onResult(true)
                }
                is ApiResult.Error -> {
                    _messages.tryEmit(result.message)
                    onResult(false)
                }
            }
            _isCancelling.value = false
        }
    }

    /** POST /api/bookings/bookings/{id}/rate/ - score 1-5 + optional feedback. */
    fun submitRating(
        id: String,
        score: Int,
        feedback: String,
        onResult: (success: Boolean) -> Unit = {}
    ) {
        val intId = id.toIntOrNull()
        if (intId == null) {
            _messages.tryEmit("Invalid booking.")
            onResult(false)
            return
        }
        if (_isRating.value) return
        viewModelScope.launch {
            _isRating.value = true

            // Rating only works on a completed booking. If the time has run out but the server
            // doesn't know yet (the Completed tab is clock-based), mark it completed first.
            val booking = getBookingById(id)
            if (booking != null && !booking.rawStatus.equals(STATUS_COMPLETED, true)) {
                if (!markCompleted(id)) {
                    _messages.tryEmit("Couldn't complete this booking yet. Please try again.")
                    _isRating.value = false
                    onResult(false)
                    return@launch
                }
            }

            when (val result = repo.rateBooking(intId, score, feedback)) {
                is ApiResult.Success -> onResult(true)
                is ApiResult.Error -> {
                    _messages.tryEmit(result.message)
                    onResult(false)
                }
            }
            _isRating.value = false
        }
    }

    fun getBookingById(id: String): BookingItem? =
        _bookings.value.find { it.id == id }
            ?: _detail.value.booking?.takeIf { it.id == id }

    /**
     * Call when the confirmation screen is closed (timeout or back press).
     * The id is removed first so the next refresh can't trigger the popup again.
     */
    fun dismissConfirmation(bookingId: String) {
        // Flip the flag first: Home re-enters composition right after this and must not see "true"
        _showConfirmation.value = false
        viewModelScope.launch { bookingPrefsManager.removeAwaiting(bookingId) }
    }

    /** Call when the declined screen is closed (either button or back press). */
    fun dismissDeclined(bookingId: String) {
        _showDeclined.value = false
        viewModelScope.launch { bookingPrefsManager.removeAwaiting(bookingId) }
    }

    /** Call on logout so the next account doesn't inherit this account's pending state. */
    fun clearSessionState() {
        viewModelScope.launch {
            bookingPrefsManager.clearAll()
            _bookings.value = emptyList()
            _showConfirmation.value = false
            _lastCreatedBooking.value = null
            _showDeclined.value = false
            _declinedBooking.value = null
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
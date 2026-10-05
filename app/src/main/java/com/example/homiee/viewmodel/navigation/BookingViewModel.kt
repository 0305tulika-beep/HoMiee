package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.BookingPrefsManager
import com.example.homiee.data.model.CreateBookingRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import com.example.homiee.ui.screens.Residentflow.BookingItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingDetailState(
    val isLoading: Boolean = true,
    val booking: BookingItem? = null,
    val errorMessage: String? = null
)

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

    // ── Create / cancel ──────────────────────────────────────────────────────
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _submitError = MutableStateFlow<String?>(null)
    val submitError: StateFlow<String?> = _submitError.asStateFlow()

    private val _isCancelling = MutableStateFlow(false)
    val isCancelling: StateFlow<Boolean> = _isCancelling.asStateFlow()

    /** One-shot messages (shown as toasts by the screens). */
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    // ── Confirmation popup (existing behaviour) ──────────────────────────────
    private val _lastCreatedBooking = MutableStateFlow<BookingItem?>(null)
    val lastCreatedBooking: StateFlow<BookingItem?> = _lastCreatedBooking.asStateFlow()

    private val _showConfirmation = MutableStateFlow(false)
    val showConfirmation: StateFlow<Boolean> = _showConfirmation.asStateFlow()

    // The create endpoint has no address field, so the GPS address is only kept on the device.
    private val localAddresses = mutableMapOf<String, String>()

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
                    val previous = _bookings.value.associateBy { it.id }
                    val fresh = result.data.data.orEmpty()
                        .mapNotNull { it.toBookingItemOrNull() }
                        .map { it.withLocalAddress() }
                    _bookings.value = fresh

                    // A booking that was pending and is now confirmed = helper accepted it
                    val accepted = fresh.firstOrNull { b ->
                        previous[b.id]?.isPending == true && !b.isPending
                    }
                    if (accepted != null) {
                        _lastCreatedBooking.value = accepted
                        _showConfirmation.value = true
                        bookingPrefsManager.markBookingConfirmed(accepted.id)
                    }
                }
                is ApiResult.Error -> _loadError.value = result.message
            }
            _isLoading.value = false
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
                    val item = result.data.data?.toBookingItem()?.withLocalAddress()
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
                        val item = dto.toBookingItem()
                        if (address.isNotBlank()) localAddresses[item.id] = address
                        val withAddress = item.withLocalAddress()
                        _bookings.value = _bookings.value.filterNot { it.id == withAddress.id } + withAddress
                        _lastCreatedBooking.value = withAddress
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

    fun getBookingById(id: String): BookingItem? =
        _bookings.value.find { it.id == id }
            ?: _detail.value.booking?.takeIf { it.id == id }

    // ── Called by Splash or Home to check if a confirmation popup is owed ──
    fun checkPendingConfirmation() {
        viewModelScope.launch {
            if (!bookingPrefsManager.hasUnseenConfirmation()) return@launch
            val id = bookingPrefsManager.getConfirmedBookingId() ?: return@launch

            var booking: BookingItem? = getBookingById(id)
            if (booking == null) {
                val intId = id.toIntOrNull() ?: return@launch
                val result = repo.bookingDetail(intId)
                if (result is ApiResult.Success) {
                    booking = result.data.data?.toBookingItem()?.withLocalAddress()
                }
            }
            _lastCreatedBooking.value = booking
            _showConfirmation.value = booking != null
        }
    }

    fun dismissConfirmation() {
        _showConfirmation.value = false
        viewModelScope.launch { bookingPrefsManager.clearConfirmation() }
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
package com.example.homiee.viewmodel.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.model.NearbyHelperDto
import com.example.homiee.data.remote.RetrofitClient
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchHelperUi(
    val id: String,
    val name: String,
    val service: String,              // "Cleaning, Laundry" (display)
    val rating: Float,
    val photoUrl: String?,
    val distanceKm: Double?,
    val minPrice: Double?,
    val services: List<String> = emptyList()   // ["Cleaning", "Laundry"] (filtering)
)

data class HelperSearchUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val helpers: List<SearchHelperUi> = emptyList()
)

// Keeps old references compiling
typealias SearchUiState = HelperSearchUiState

@OptIn(FlowPreview::class)
class HelperSearchViewModel : ViewModel() {

    private val repo = BookingRepository()

    private val _uiState = MutableStateFlow(HelperSearchUiState())
    val uiState: StateFlow<HelperSearchUiState> = _uiState.asStateFlow()

    private val query     = MutableStateFlow("")
    private val retryTick = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            combine(
                // debounce only while typing; clearing the box fires immediately
                query.map { it.trim() }
                    .debounce { if (it.isEmpty()) 0L else 400L }
                    .distinctUntilChanged(),
                retryTick
            ) { q, _ -> q }
                // collectLatest cancels the in-flight request when the query changes
                .collectLatest { q -> fetch(q) }
        }
    }

    fun setQuery(value: String) { query.value = value }
    fun retry()                 { retryTick.value++ }

    /** blank -> nearby endpoint, otherwise -> /helpers/search/?q= */
    private suspend fun fetch(q: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        val result = if (q.isEmpty()) repo.nearbyHelpers() else repo.search(q)

        _uiState.value = when (result) {
            is ApiResult.Success -> HelperSearchUiState(
                isLoading = false,
                helpers = result.data.data.orEmpty().map { it.toUi() }
            )
            is ApiResult.Error -> HelperSearchUiState(
                isLoading = false,
                errorMessage = result.message
            )
        }
    }

    private fun NearbyHelperDto.toUi(): SearchHelperUi {
        val serviceNames = services.orEmpty().mapNotNull { it.name }
        val prices = services.orEmpty().mapNotNull { it.price_per_hour?.toDoubleOrNull() }
        return SearchHelperUi(
            id = helper_id.toString(),
            name = "${fname.orEmpty()} ${lname.orEmpty()}".trim().ifBlank { username ?: "Helper" },
            service = serviceNames.joinToString(", "),
            rating = avg_rating?.toFloatOrNull() ?: 0f,
            photoUrl = profile_photo?.let {
                if (it.startsWith("http")) it
                else RetrofitClient.BASE_URL.trimEnd('/') + it
            },
            distanceKm = distance_km,
            minPrice = prices.minOrNull(),
            services = serviceNames
        )
    }
}
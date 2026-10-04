package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.model.NearbyHelperDto
import com.example.homiee.data.remote.RetrofitClient
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NearbyHelperUi(
    val id: String,
    val name: String,
    val services: String,
    val rating: String,
    val photoUrl: String?
)

data class NearbyHelpersUiState(
    val isLoading:    Boolean = true,
    val helpers:      List<NearbyHelperUi> = emptyList(),
    val errorMessage: String? = null
)

class NearbyHelpersViewModel : ViewModel() {

    private val repository = BookingRepository()

    private val _uiState = MutableStateFlow(NearbyHelpersUiState())
    val uiState: StateFlow<NearbyHelpersUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.nearbyHelpers()) {
                is ApiResult.Success -> {
                    val list = result.data.data.orEmpty().map { it.toUi() }
                    _uiState.value = NearbyHelpersUiState(isLoading = false, helpers = list)
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}

private fun NearbyHelperDto.toUi(): NearbyHelperUi {
    val fullName = listOfNotNull(fname, lname).filter { it.isNotBlank() }.joinToString(" ")
    val photo = profile_photo?.takeIf { it.isNotBlank() }?.let { path ->
        if (path.startsWith("http")) path
        else RetrofitClient.BASE_URL.trimEnd('/') + "/" + path.trimStart('/')
    }
    return NearbyHelperUi(
        id       = helper_id.toString(),
        name     = fullName.ifBlank { username.orEmpty() },
        services = services.orEmpty().mapNotNull { it.name?.takeIf { n -> n.isNotBlank() } }.joinToString(", "),
        rating   = avg_rating?.toDoubleOrNull()?.let { "%.1f".format(it) } ?: "New",
        photoUrl = photo
    )
}
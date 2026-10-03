package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.remote.RetrofitClient
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.ResidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading:    Boolean = true,
    val name:         String  = "",
    val email:        String  = "",
    val address:      String  = "",
    val photoUrl:     String? = null,
    val errorMessage: String? = null
)

class ProfileViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = ResidentRepository()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            // Name + email come from local storage (the GET response doesn't include them)
            val name = listOfNotNull(tokenManager.getFirstName(), tokenManager.getLastName())
                .filter { it.isNotBlank() }
                .joinToString(" ")
            val email = tokenManager.getEmail().orEmpty()

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                name = name,
                email = email
            )

            when (val result = repository.getProfile()) {
                is ApiResult.Success -> {
                    val p = result.data
                    val address = listOfNotNull(p.house_no, p.area, p.city, p.pincode)
                        .filter { it.isNotBlank() }
                        .joinToString(", ")
                    val photoUrl = p.profile_photo
                        ?.takeIf { it.isNotBlank() }
                        ?.let { path ->
                            if (path.startsWith("http")) path
                            else RetrofitClient.BASE_URL.trimEnd('/') + "/" + path.trimStart('/')
                        }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        address = address,
                        photoUrl = photoUrl
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}

class ProfileViewModelFactory(
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProfileViewModel(TokenManager(context.applicationContext)) as T
    }
}
package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.model.LoginRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val role:         String? = null,   // ← NEW: NavGraph reads this to pick Home
    val errorMessage: String? = null
)

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {   // ← role param removed
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter both email and password."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = repository.login(LoginRequest(identifier = email, password = password))

            when (result) {
                is ApiResult.Success -> {
                    val tokens = result.data.data?.tokens
                    val role   = result.data.data?.user?.role

                    if (tokens != null) {
                        tokenManager.saveTokens(tokens.access, tokens.refresh)
                        if (role != null) {
                            tokenManager.saveRole(role)
                        }
                        tokenManager.markFormsCompleted()   // returning users already onboarded

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            role = role
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Something went wrong. Please try again."
                        )
                    }
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

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}

class LoginViewModelFactory(
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(TokenManager(context.applicationContext)) as T
    }
}
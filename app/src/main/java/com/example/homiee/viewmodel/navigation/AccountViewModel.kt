package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.SessionManager
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AccountActionUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val errorMessage: String? = null
)

class AccountViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = AuthRepository()

    private val _logoutState = MutableStateFlow(AccountActionUiState())
    val logoutState: StateFlow<AccountActionUiState> = _logoutState

    private val _deactivateState = MutableStateFlow(AccountActionUiState())
    val deactivateState: StateFlow<AccountActionUiState> = _deactivateState

    private val _deleteState = MutableStateFlow(AccountActionUiState())
    val deleteState: StateFlow<AccountActionUiState> = _deleteState

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = _logoutState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.logout()) {
                is ApiResult.Success -> {
                    clearLocalSession()
                    _logoutState.value = _logoutState.value.copy(isLoading = false, isSuccess = true)
                }
                is ApiResult.Error -> {
                    // Even if server call fails (e.g. token already expired), clear local session anyway
                    clearLocalSession()
                    _logoutState.value = _logoutState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun deactivateAccount(password: String) {
        if (password.isBlank()) {
            _deactivateState.value = _deactivateState.value.copy(errorMessage = "Please enter your password.")
            return
        }
        viewModelScope.launch {
            _deactivateState.value = _deactivateState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.deactivateAccount(password)) {
                is ApiResult.Success -> {
                    clearLocalSession()
                    _deactivateState.value = _deactivateState.value.copy(isLoading = false, isSuccess = true)
                }
                is ApiResult.Error -> {
                    _deactivateState.value = _deactivateState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun deleteAccount(password: String) {
        if (password.isBlank()) {
            _deleteState.value = _deleteState.value.copy(errorMessage = "Please enter your password.")
            return
        }
        viewModelScope.launch {
            _deleteState.value = _deleteState.value.copy(isLoading = true, errorMessage = null)
            when (val result = repository.deleteAccount(password)) {
                is ApiResult.Success -> {
                    clearLocalSession()
                    _deleteState.value = _deleteState.value.copy(isLoading = false, isSuccess = true)
                }
                is ApiResult.Error -> {
                    _deleteState.value = _deleteState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private suspend fun clearLocalSession() {
        tokenManager.clearTokens()
        SessionManager.clear()
    }

    fun resetLogoutState()     { _logoutState.value = AccountActionUiState() }
    fun resetDeactivateState() { _deactivateState.value = AccountActionUiState() }
    fun resetDeleteState()     { _deleteState.value = AccountActionUiState() }
}

class AccountViewModelFactory(
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AccountViewModel(TokenManager(context.applicationContext)) as T
    }
}
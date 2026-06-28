package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.model.ResendOtpRequest
import com.example.homiee.data.model.VerifyOtpRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OtpUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val errorMessage: String? = null
)

data class ResendUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val errorMessage: String? = null
)

class OtpViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState

    private val _resendState = MutableStateFlow(ResendUiState())
    val resendState: StateFlow<ResendUiState> = _resendState

    fun verifyOtp(email: String, otp: String) {   // ← role param removed
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = repository.verifyOtp(VerifyOtpRequest(identifier = email, otp = otp))

            when (result) {
                is ApiResult.Success -> {
                    val tokens = result.data.data?.tokens
                    if (tokens != null) {
                        tokenManager.saveTokens(tokens.access, tokens.refresh)
                        // role NOT saved here — Choice screen sets it after this
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
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

    fun resendOtp(email: String) {
        viewModelScope.launch {
            _resendState.value = _resendState.value.copy(isLoading = true, errorMessage = null)

            val result = repository.resendOtp(ResendOtpRequest(identifier = email))

            when (result) {
                is ApiResult.Success -> {
                    _resendState.value = _resendState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                is ApiResult.Error -> {
                    _resendState.value = _resendState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value     = OtpUiState()
        _resendState.value = ResendUiState()
    }
}

class OtpViewModelFactory(
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return OtpViewModel(TokenManager(context.applicationContext)) as T
    }
}
package com.example.homiee.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class OtpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository   = AuthRepository()
    private val tokenManager = TokenManager(application.applicationContext)

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState

    private val _resendState = MutableStateFlow(ResendUiState())
    val resendState: StateFlow<ResendUiState> = _resendState

    fun verifyOtp(email: String, otp: String, role: String) {   // ← role param added
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = repository.verifyOtp(VerifyOtpRequest(identifier = email, otp = otp))

            when (result) {
                is ApiResult.Success -> {
                    val tokens = result.data.data?.tokens
                    if (tokens != null) {
                        tokenManager.saveTokens(tokens.access, tokens.refresh, role)   // ← role passed
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
        _uiState.value   = OtpUiState()
        _resendState.value = ResendUiState()
    }
}
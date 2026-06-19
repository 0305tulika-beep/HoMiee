package com.example.homiee.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.model.RegisterRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // Stored after successful register so OtpScreen can read it
    var registeredEmail: String = ""
        private set

    fun register(
        firstName: String,
        lastName:  String,
        email:     String,
        mobile:    String,
        username:  String,
        password:  String,
        password2: String
    ) {
        // Basic local validation
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() ||
            mobile.isBlank() || username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please fill in all fields."
            )
            return
        }
        if (password != password2) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Passwords do not match."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = repository.register(
                RegisterRequest(
                    fname     = firstName,
                    lname     = lastName,
                    email     = email,
                    mobile    = mobile,
                    username  = username,
                    password  = password,
                    password2 = password2
                )
            )

            when (result) {
                is ApiResult.Success -> {
                    registeredEmail = email   // save for OTP screen
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

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}
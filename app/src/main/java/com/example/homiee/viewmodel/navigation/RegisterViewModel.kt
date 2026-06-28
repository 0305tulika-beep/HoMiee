package com.example.homiee.viewmodel

import androidx.lifecycle.ViewModel
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

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    var registeredEmail: String = ""
        private set

    // Kept so OtpScreen → back → Signup can restore fields if you wire that up later
    var firstNameValue: String = ""
    var lastNameValue:  String = ""
    var mobileValue:    String = ""
    var usernameValue:  String = ""

    fun register(
        firstName: String,
        lastName:  String,
        email:     String,
        mobile:    String,
        username:  String,
        password:  String,
        password2: String
    ) {
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

        firstNameValue = firstName
        lastNameValue  = lastName
        mobileValue    = mobile
        usernameValue  = username

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
                    registeredEmail = email
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
package com.example.homiee.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homiee.data.local.TokenManager
import com.example.homiee.data.model.RegisterRequest
import com.example.homiee.data.repository.ApiResult
import com.example.homiee.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class RegisterUiState(
    val isLoading:    Boolean = false,
    val isSuccess:    Boolean = false,
    val errorMessage: String? = null
)

class RegisterViewModel(
    private val tokenManager: TokenManager   // CHANGED: non-null, so the name is always saved
) : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    var registeredEmail: String = ""
        private set

    var firstNameValue: String = ""
    var lastNameValue:  String = ""

    fun register(
        firstName: String,
        lastName:  String,
        email:     String,
        password:  String,
        password2: String
    ) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
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

        val cleanFirst = firstName.trim()
        val cleanLast  = lastName.trim()
        val cleanEmail = email.trim()

        firstNameValue = cleanFirst
        lastNameValue  = cleanLast

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            attemptRegister(cleanFirst, cleanLast, cleanEmail, password, password2, retriesLeft = 2)
        }
    }

    private suspend fun attemptRegister(
        firstName:   String,
        lastName:    String,
        email:       String,
        password:    String,
        password2:   String,
        retriesLeft: Int
    ) {
        val autoUsername = generateSafeUsername(firstName, lastName)

        val result = repository.register(
            RegisterRequest(
                fname     = firstName,
                lname     = lastName,
                email     = email,
                username  = autoUsername,
                password  = password,
                password2 = password2
            )
        )

        when (result) {
            is ApiResult.Success -> {
                registeredEmail = result.data.data?.identifier ?: email
                // Persist for the Profile screen (GET /residents/profile/ doesn't return these)
                tokenManager.saveUserName(firstName, lastName)
                tokenManager.saveEmail(registeredEmail)
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }
            is ApiResult.Error -> {
                val isUsernameIssue = result.message.contains("username", ignoreCase = true)
                if (isUsernameIssue && retriesLeft > 0) {
                    // Silent retry with a freshly generated username — user never sees this field
                    attemptRegister(firstName, lastName, email, password, password2, retriesLeft - 1)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = if (isUsernameIssue)
                            "Something went wrong on our end. Please try again."
                        else
                            result.message
                    )
                }
            }
        }
    }

    /**
     * Generates a username that's safe against common Django-style constraints:
     *  - starts with a LETTER (never a digit)
     *  - only letters + digits (no dots, hyphens, special chars)
     *  - length kept between ~6–14 characters (safely inside typical 4–30 char limits)
     *  - lowercase, to avoid any case-sensitivity uniqueness surprises
     */
    private fun generateSafeUsername(firstName: String, lastName: String): String {
        val base = (firstName.take(4) + lastName.take(4))
            .filter { it.isLetter() }
            .lowercase()
            .ifEmpty { "user" }
        val suffix = Random.nextInt(100, 999)
        return "$base$suffix".take(14)
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}

// NEW: builds RegisterViewModel with a real TokenManager
class RegisterViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RegisterViewModel(TokenManager(context.applicationContext)) as T
    }
}
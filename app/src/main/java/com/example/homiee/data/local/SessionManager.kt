package com.example.homiee.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    @Volatile
    var accessToken: String? = null
    private val _sessionExpired = MutableStateFlow(false)
    val sessionExpired: StateFlow<Boolean> = _sessionExpired.asStateFlow()

    fun clear() {
        accessToken = null
    }
    /** Called by TokenAuthenticator when a request 401s and can't be recovered. */
    fun notifySessionExpired() {
        accessToken = null
        _sessionExpired.value = true
    }

    /** Called once the UI has reacted (navigated to login) so it doesn't fire again. */
    fun consumeSessionExpired() {
        _sessionExpired.value = false
    }
}
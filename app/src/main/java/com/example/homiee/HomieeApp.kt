package com.example.homiee

import android.app.Application
import com.example.homiee.data.local.SessionManager
import com.example.homiee.data.local.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomieeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val tokenManager = TokenManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            SessionManager.accessToken = tokenManager.getAccessToken()
        }
    }
}
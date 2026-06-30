package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.ui.components.GradientTextField
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.White

@Composable
fun ForgotPasswordScreen(
    onBack:     () -> Unit = {},
    onContinue: (String) -> Unit = {}
) {
    HideSystemBars(lightIcons = true)

    var email by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // Full-screen bg3 background — same as login/signup
        Image(
            painter            = painterResource(R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // ── Title ─────────────────────────────────────────────────────
            Text(
                text       = "Find Your Account",
                fontSize   = 32.sp,
                fontWeight = FontWeight.Bold,
                color      = White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text     = "Enter your email",
                fontSize = 15.sp,
                color    = White.copy(alpha = 0.80f)
            )

            Spacer(Modifier.height(36.dp))

            // ── Email field — same styling as LoginScreen ───────────────────
            GradientTextField(
                value         = email,
                onValueChange = { email = it },
                placeholder   = "Email",
                keyboardType  = KeyboardType.Email
            )

            Spacer(Modifier.height(32.dp))

            // ── Continue button — same styling as LoginScreen's Log In button ──
            HomieeButton(
                text    = "Continue",
                enabled = email.isNotBlank(),
                onClick = { if (email.isNotBlank()) onContinue(email.trim()) }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
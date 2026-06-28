
package com.example.homiee.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.homiee.ui.theme.HomieeTheme

// ── 1. Splash ──────────────────────────────────────────────
@Preview(showBackground = true, name = "Splash Screen")
@Composable
fun SplashScreenPreview() {
    HomieeTheme {
        SplashScreen(onFinished = {})
    }
}

// ── 2. Login ───────────────────────────────────────────────
@Preview(showBackground = true, name = "Login Screen")
@Composable
fun LoginScreenPreview() {
    HomieeTheme {
        LoginScreen(
            navController  = rememberNavController(),
            onLoginSuccess = {}
        )
    }
}


// ── 4. Sign Up ─────────────────────────────────────────────
@Preview(showBackground = true, name = "Sign Up")
@Composable
fun SignUpScreenPreview() {
    HomieeTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

// ── 5. OTP ─────────────────────────────────────────────────
@Preview(showBackground = true, name = "OTP Screen")
@Composable
fun OtpScreenPreview() {
    HomieeTheme {
        OtpScreen(
            email = "test@example.com",
            onConfirm = {}
        )
    }
}
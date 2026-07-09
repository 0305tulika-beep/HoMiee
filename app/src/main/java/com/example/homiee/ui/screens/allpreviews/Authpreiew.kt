package com.example.homiee.ui.screens.allpreviews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.homiee.ui.screens.auth.ForgotPasswordScreen
import com.example.homiee.ui.screens.auth.LoginScreen
import com.example.homiee.ui.screens.auth.OtpScreen
import com.example.homiee.ui.screens.auth.SignUpScreen
import com.example.homiee.ui.screens.auth.SplashScreen
import com.example.homiee.ui.screens.auth.TermsAndConditionsDialog
import com.example.homiee.ui.theme.HomieeTheme

// ── 1. Splash ────────────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Splash Screen")
@Composable
fun SplashScreenPreview() {
    HomieeTheme {
        SplashScreen(onFinished = {})
    }
}

// ── 2. Login ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Login Screen")
@Composable
fun LoginScreenPreview() {
    HomieeTheme {
        LoginScreen(
            navController = rememberNavController(),
            onLoginSuccess = {},
            onForgotPassword = {}
        )
    }
}

// ── 3. Forgot Password ────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Forgot Password Screen")
@Composable
fun ForgotPasswordScreenPreview() {
    HomieeTheme {
        ForgotPasswordScreen(
            onBack = {},
            onContinue = {}
        )
    }
}

// ── 4. Sign Up ────────────────────────────────────────────────────────────────
@Preview(showBackground = true, name = "Sign Up")
@Composable
fun SignUpScreenPreview() {
    HomieeTheme {
        SignUpScreen(navController = rememberNavController())
    }
}

// ── 5. OTP — Login flow ───────────────────────────────────────────────────────
@Preview(showBackground = true, name = "OTP Screen — Login Flow")
@Composable
fun OtpScreenLoginPreview() {
    HomieeTheme {
        OtpScreen(
            email     = "test@example.com",
            flow      = "login",
            onConfirm = {}
        )
    }
}

// ── 6. OTP — Signup flow (this is the one that can trigger the T&C popup) ────
@Preview(showBackground = true, name = "OTP Screen — Signup Flow")
@Composable
fun OtpScreenSignupPreview() {
    HomieeTheme {
        OtpScreen(
            email     = "test@example.com",
            flow      = "signup",
            onConfirm = {}
        )
    }
}

// ── 7. Terms & Conditions (now a popup dialog, previewed standalone) ─────────
@Preview(showBackground = true, name = "Terms & Conditions Dialog")
@Composable
fun TermsAndConditionsDialogPreview() {
    HomieeTheme {
        TermsAndConditionsDialog(
            onAgree = {}
        )
    }
}
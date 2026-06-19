// ============================================================
// PREVIEWS for Auth screens
// Paste each block at the BOTTOM of its matching file:
//   SplashScreen.kt, LoginScreen.kt, ChoiceScreen.kt,
//   SignUpResidentScreen.kt, SignUpHelperScreen.kt, OtpScreen.kt
//
// OR: create one new file called "AuthPreviews.kt" in
// ui/screens/auth/ and paste this entire file as-is.
// ============================================================

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
            navController = rememberNavController(),
            role = "resident"
        )
    }
}

// ── 3. Choice ──────────────────────────────────────────────
@Preview(showBackground = true, name = "Choice Screen")
@Composable
fun ChoiceScreenPreview() {
    HomieeTheme {
        ChoiceScreen(navController = rememberNavController())
    }
}

// ── 4. Sign Up — Resident ─────────────────────────────────
@Preview(showBackground = true, name = "Sign Up Resident")
@Composable
fun SignUpResidentScreenPreview() {
    HomieeTheme {
        SignUpResidentScreen(navController = rememberNavController())
    }
}

// ── 5. Sign Up — Helper ───────────────────────────────────
@Preview(showBackground = true, name = "Sign Up Helper")
@Composable
fun SignUpHelperScreenPreview() {
    HomieeTheme {
        SignUpHelperScreen(navController = rememberNavController())
    }
}

// ── 6. OTP ─────────────────────────────────────────────────
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
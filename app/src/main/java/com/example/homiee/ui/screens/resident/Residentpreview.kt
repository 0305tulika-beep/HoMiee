package com.example.homiee.ui.screens.resident

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

@Preview(showBackground = true, name = "Step 1 — Address")
@Composable
fun ResFormAddressScreenPreview() {
    HomieeTheme { ResFormAddressScreen(onNext = {}) }
}

@Preview(showBackground = true, name = "Step 2 — Emergency Contact")
@Composable
fun ResFormEmergencyScreenPreview() {
    HomieeTheme { ResFormEmergencyScreen(onNext = {}) }
}

@Preview(showBackground = true, name = "Step 3 — Identity Verification")
@Composable
fun ResFormIdentityScreenPreview() {
    HomieeTheme { ResFormIdentityScreen(onNext = {}) }
}

@Preview(showBackground = true, name = "Step 4 — Photo")
@Composable
fun ResFormPhotoScreenPreview() {
    HomieeTheme { ResFormPhotoScreen(onFinish = {}) }
}
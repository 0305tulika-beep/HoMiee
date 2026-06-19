// ============================================================
// PREVIEWS for Resident form screens
// Create one new file called "ResidentPreviews.kt" in
// ui/screens/resident/ and paste this entire file as-is.
// ============================================================

package com.example.homiee.ui.screens.resident

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

// ── Step 1 — Address ──────────────────────────────────────
@Preview(showBackground = true, name = "Resident - Address")
@Composable
fun ResFormAddressScreenPreview() {
    HomieeTheme {
        ResFormAddressScreen(onNext = {})
    }
}

// ── Step 2 — Services ─────────────────────────────────────
@Preview(showBackground = true, name = "Resident - Services")
@Composable
fun ResFormServicesScreenPreview() {
    HomieeTheme {
        ResFormServicesScreen(onNext = {})
    }
}

// ── Step 3 — Schedule ─────────────────────────────────────
@Preview(showBackground = true, name = "Resident - Schedule")
@Composable
fun ResFormScheduleScreenPreview() {
    HomieeTheme {
        ResFormScheduleScreen(onNext = {})
    }
}

// ── Step 4 — Identity ─────────────────────────────────────
@Preview(showBackground = true, name = "Resident - Identity")
@Composable
fun ResFormIdentityScreenPreview() {
    HomieeTheme {
        ResFormIdentityScreen(onNext = {})
    }
}

// ── Step 5 — Photo ─────────────────────────────────────────
@Preview(showBackground = true, name = "Resident - Photo")
@Composable
fun ResFormPhotoScreenPreview() {
    HomieeTheme {
        ResFormPhotoScreen(onFinish = {})
    }
}
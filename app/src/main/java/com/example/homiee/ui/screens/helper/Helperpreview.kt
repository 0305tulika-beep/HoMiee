// ============================================================
// PREVIEWS for Helper form screens
// Create one new file called "HelperPreviews.kt" in
// ui/screens/helper/ and paste this entire file as-is.
// ============================================================

package com.example.homiee.ui.screens.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

// ── Step 1 — Identity Verification ────────────────────────
@Preview(showBackground = true, name = "Helper - Identity")
@Composable
fun HelpFormIdentityScreenPreview() {
    HomieeTheme {
        HelpFormIdentityScreen(onNext = {})
    }
}

// ── Step 2 — Services Offered ─────────────────────────────
@Preview(showBackground = true, name = "Helper - Services")
@Composable
fun HelpFormServicesScreenPreview() {
    HomieeTheme {
        HelpFormServicesScreen(onNext = {})
    }
}

// ── Step 3 — Experience & Skills ──────────────────────────
@Preview(showBackground = true, name = "Helper - Skills")
@Composable
fun HelpFormSkillsScreenPreview() {
    HomieeTheme {
        HelpFormSkillsScreen(onNext = {})
    }
}

// ── Step 4 — Availability & Trust ─────────────────────────
@Preview(showBackground = true, name = "Helper - Availability")
@Composable
fun HelpFormAvailabilityScreenPreview() {
    HomieeTheme {
        HelpFormAvailabilityScreen(onFinish = {})
    }
}
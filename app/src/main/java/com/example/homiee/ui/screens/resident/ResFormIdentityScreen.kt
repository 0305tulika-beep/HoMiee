package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.homiee.ui.components.DocumentUploadCard


@Composable
fun ResFormIdentityScreen(
    onUploadAadhaar: () -> Unit,
    onUploadPan: () -> Unit,
    onNext: () -> Unit
) {
    OnboardingStepScaffold(
        currentStep = 3,
        title = "Identity Verification",
        subtitle = "Upload your Aadhaar card and PAN card for verification",
        buttonText = "Next",
        onButtonClick = onNext
    ) {
        DocumentUploadCard(
            icon = Icons.Default.Badge,
            title = "Aadhaar Card",
            subtitle = "Upload front side of Aadhaar card",
            onUploadClick = onUploadAadhaar
        )
        Spacer(modifier = Modifier.height(16.dp))
        DocumentUploadCard(
            icon = Icons.Default.Badge,
            title = "PAN Card (Optional)",
            subtitle = "Upload PAN card (if available)",
            onUploadClick = onUploadPan
        )
    }
}
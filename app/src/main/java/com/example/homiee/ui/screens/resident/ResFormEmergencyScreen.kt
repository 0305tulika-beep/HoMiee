package com.example.homiee.ui.screens.resident

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ResFormEmergencyScreen(
    contactName: String,
    onContactNameChange: (String) -> Unit,
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onNext: () -> Unit
) {
    OnboardingStepScaffold(
        currentStep = 2,
        title = "Emergency Contact",
        subtitle = "Add a contact person we can reach in case of emergency",
        buttonText = "Next",
        onButtonClick = onNext
    ) {
        HomieeFormField(
            label = "Contact Name",
            value = contactName,
            onValueChange = onContactNameChange,
            placeholder = "Enter full name",
            leadingIcon = Icons.Default.Person
        )
        HomieeFormField(
            label = "Mobile Number",
            value = mobileNumber,
            onValueChange = onMobileNumberChange,
            placeholder = "Enter mobile number",
            leadingIcon = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone
        )
    }
}

package com.example.homiee.ui.screens.resident

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.CardTextField
import com.example.homiee.ui.theme.TextPrimary

@Composable
fun ResFormEmergencyScreen(onNext: () -> Unit) {
    var emergencyName   by remember { mutableStateOf("") }
    var emergencyMobile by remember { mutableStateOf("") }

    ResFormShell(title = "Emergency Contact", step = 2, onNext = onNext) {

        Text(
            "EMERGENCY CONTACT",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        CardTextField(
            value         = emergencyName,
            onValueChange = { emergencyName = it },
            placeholder   = "Enter name",
            label         = "NAME"
        )

        CardTextField(
            value         = emergencyMobile,
            onValueChange = { emergencyMobile = it },
            placeholder   = "Enter mobile",
            label         = "MOBILE NO.",
            keyboardType  = KeyboardType.Phone
        )
    }
}
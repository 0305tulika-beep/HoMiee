package com.example.homiee.ui.screens.helper

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.CardTextField
import com.example.homiee.ui.theme.BorderColor
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextPrimary

/**
 * STEP 4 — AVAILABILITY & TRUST
 * Work preference, working hours dropdown, areas, emergency contact.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFormAvailabilityScreen(onFinish: () -> Unit) {
    var workPreference  by remember { mutableStateOf("") }
    var workingHours    by remember { mutableStateOf("") }
    var hoursExpanded   by remember { mutableStateOf(false) }
    var areasWilling    by remember { mutableStateOf("") }
    var emergencyName   by remember { mutableStateOf("") }
    var emergencyMobile by remember { mutableStateOf("") }

    val hourOptions = listOf(
        "Part-time (4 hrs/day)", "Full-time (8 hrs/day)",
        "Flexible", "Weekends only"
    )

    HelpFormShell(title = "Availability & Trust", step = 4, onNext = onFinish, nextLabel = "Submit") {
        CardTextField(
            value         = workPreference,
            onValueChange = { workPreference = it },
            placeholder   = "e.g. Full-time / Part-time / Live-in",
            label         = "WORK PREFERENCE"
        )

        Column {
            Text(
                "WORKING HOURS",
                fontWeight = FontWeight.Bold,
                fontSize   = 13.sp,
                color      = TextPrimary
            )
            Spacer(Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded         = hoursExpanded,
                onExpandedChange = { hoursExpanded = it }
            ) {
                OutlinedTextField(
                    value         = workingHours.ifEmpty { "Select hours" },
                    onValueChange = {},
                    readOnly      = true,
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hoursExpanded) },
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = GreenTint,
                        unfocusedContainerColor = GreenTint,
                        focusedBorderColor      = GreenLight,
                        unfocusedBorderColor    = BorderColor
                    ),
                    shape    = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded         = hoursExpanded,
                    onDismissRequest = { hoursExpanded = false }
                ) {
                    hourOptions.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            workingHours  = it
                            hoursExpanded = false
                        })
                    }
                }
            }
        }

        CardTextField(
            value         = areasWilling,
            onValueChange = { areasWilling = it },
            placeholder   = "e.g. Andheri, Bandra, Juhu",
            label         = "AREAS WILLING TO WORK IN"
        )

        Divider(color = Color(0xFFE0E0E0))

        Text(
            "EMERGENCY CONTACT",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        CardTextField(emergencyName, { emergencyName = it }, "Enter name", label = "NAME")
        CardTextField(
            value         = emergencyMobile,
            onValueChange = { emergencyMobile = it },
            placeholder   = "Enter mobile",
            label         = "MOBILE NO.",
            keyboardType  = KeyboardType.Phone
        )
    }
}
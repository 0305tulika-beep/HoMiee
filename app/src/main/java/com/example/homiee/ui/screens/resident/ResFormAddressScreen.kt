package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.HomieeColors
import com.example.homiee.ui.components.HomieeFormField

@Composable
fun ResFormAddressScreen(
    houseNo: String,
    onHouseNoChange: (String) -> Unit,
    area: String,
    onAreaChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    pincode: String,
    onPincodeChange: (String) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onNext: () -> Unit,
    showValidationError: Boolean = false,   // NEW
    isLoading: Boolean = false,             // NEW
    errorMessage: String? = null            // NEW
) {
    OnboardingStepScaffold(
        currentStep = 1,
        title = "Your Address",
        subtitle = "Please enter your current address details",
        buttonText = "Next",
        onButtonClick = onNext,
        isLoading = isLoading,
        errorMessage = errorMessage
    ) {
        HomieeFormField(
            label = "House / Apt No.",
            value = houseNo,
            onValueChange = onHouseNoChange,
            placeholder = "Enter house / apt no.",
            leadingIcon = Icons.Default.Home
        )
        if (showValidationError && houseNo.isBlank()) FieldWarning("House / Apt No. is required")

        HomieeFormField(
            label = "Area / Locality",
            value = area,
            onValueChange = onAreaChange,
            placeholder = "Enter area / locality",
            leadingIcon = Icons.Default.LocationOn
        )
        if (showValidationError && area.isBlank()) FieldWarning("Area / Locality is required")

        HomieeFormField(
            label = "City",
            value = city,
            onValueChange = onCityChange,
            placeholder = "Enter city",
            leadingIcon = Icons.Default.LocationCity
        )
        if (showValidationError && city.isBlank()) FieldWarning("City is required")

        HomieeFormField(
            label = "Pincode",
            value = pincode,
            onValueChange = onPincodeChange,
            placeholder = "Enter pincode",
            leadingIcon = Icons.Default.Numbers,
            keyboardType = KeyboardType.Number
        )
        if (showValidationError && pincode.isBlank()) FieldWarning("Pincode is required")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, HomieeColors.BorderGray, RoundedCornerShape(10.dp))
                .padding(14.dp)
                .clip(RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = null, tint = HomieeColors.PrimaryDark)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Use Current Location (GPS)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text("Auto-fill your current address", fontSize = 12.sp, color = HomieeColors.TextGray)
            }
            IconButton(onClick = onUseCurrentLocation) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Use current location")
            }
        }
    }
}

// NEW: shared small red helper text used across all onboarding forms
@Composable
fun FieldWarning(text: String) {
    Text(
        text = text,
        color = Color(0xFFDC2626),
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 4.dp, top = 2.dp, bottom = 8.dp)
    )
}
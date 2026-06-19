package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
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

// Colors for the GPS pill button, matched to the design mock.
// Swap these for theme colors (e.g. BlueLight / BlueMid) if your theme defines them.
private val GpsBlue   = Color(0xFF29B6F6)
private val GpsBlueBg = Color(0xFFE1F5FE)

/**
 * STEP 1 — YOUR ADDRESS
 * Collects: House/Apt No., Area/Locality, City, Pincode
 * GPS button launches location permission flow.
 *
 * HOW TO ADD GPS:
 *   - Add ACCESS_FINE_LOCATION to AndroidManifest.xml
 *   - Use rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())
 *   - On grant: use FusedLocationProviderClient to get lat/lng → reverse geocode
 */
@Composable
fun ResFormAddressScreen(onNext: () -> Unit) {
    var house   by remember { mutableStateOf("") }
    var area    by remember { mutableStateOf("") }
    var city    by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }

    ResFormShell(title = "YOUR ADDRESS", step = 1, onNext = onNext) {
        CardTextField(house,   { house   = it }, "Enter house/apt no.", label = "HOUSE / APT NO.")
        CardTextField(area,    { area    = it }, "Enter area/locality", label = "AREA / LOCALITY")
        CardTextField(city,    { city    = it }, "Enter city",          label = "CITY")
        CardTextField(
            value         = pincode,
            onValueChange = { pincode = it },
            placeholder   = "Enter pincode",
            label         = "PINCODE",
            keyboardType  = KeyboardType.Number
        )

        // GPS button — light blue filled pill, blue border, blue icon + text
        OutlinedButton(
            onClick  = { /* TODO: request location permission */ },
            shape    = RoundedCornerShape(50),
            colors   = ButtonDefaults.outlinedButtonColors(
                containerColor = GpsBlueBg,
                contentColor   = GpsBlue
            ),
            border   = BorderStroke(1.5.dp, GpsBlue),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.MyLocation,
                contentDescription = null,
                tint               = GpsBlue,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = "Use Current Location (GPS)",
                fontWeight = FontWeight.SemiBold,
                fontSize   = 16.sp,
                color      = GpsBlue
            )
        }
    }
}
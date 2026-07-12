package com.example.homiee.ui.screens.resident

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.homiee.ui.components.HomieeColors
import com.example.homiee.ui.components.HomieeFormField
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import java.util.Locale

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
    onUseCurrentLocation: (latitude: String, longitude: String) -> Unit,
    onNext: () -> Unit,
    showValidationError: Boolean = false,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isFetchingLocation by remember { mutableStateOf(false) }
    var locationStatus by remember { mutableStateOf<String?>(null) }   // null = idle, else shown under the row
    var locationStatusIsError by remember { mutableStateOf(false) }

    fun fetchAndReport() {
        isFetchingLocation = true
        locationStatus = null
        coroutineScope.launch {
            val result = getCurrentLocationOrNull(context)
            isFetchingLocation = false
            if (result != null) {
                // CHANGED: round to 6 decimal places so the payload fits the
                // backend's DecimalField limit (max 9 total digits). Raw
                // Location.latitude/.longitude are full-precision doubles
                // (e.g. 26.846713345678) which exceed that limit and caused
                // HTTP 400 "no more than 9 digits in total" errors.
                val lat = String.format(Locale.US, "%.6f", result.latitude)
                val lng = String.format(Locale.US, "%.6f", result.longitude)
                onUseCurrentLocation(lat, lng)
                locationStatusIsError = false
                locationStatus = "Location detected successfully"
            } else {
                locationStatusIsError = true
                locationStatus = "Couldn't get your location. Make sure location services are on and try again."
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchAndReport()
        } else {
            locationStatusIsError = true
            locationStatus = "Location permission is required to auto-fill your address."
        }
    }

    fun onUseCurrentLocationClicked() {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            fetchAndReport()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

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
            if (isFetchingLocation) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = HomieeColors.PrimaryDark
                )
            } else {
                IconButton(onClick = { onUseCurrentLocationClicked() }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Use current location")
                }
            }
        }

        locationStatus?.let { status ->
            Text(
                text = status,
                color = if (locationStatusIsError) Color(0xFFDC2626) else Color(0xFF16A34A),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }
}

/**
 * Tries getLastKnownLocation first (instant, no network wait). If nothing is cached
 * (common on fresh emulators/devices), falls back to a single fresh location request
 * with a short timeout. Returns null if location can't be determined at all
 * (permission missing, GPS/network both disabled, or timeout).
 */
private suspend fun getCurrentLocationOrNull(context: Context): Location? {
    val hasFine = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val hasCoarse = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    if (!hasFine && !hasCoarse) return null

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        ?: return null

    // 1) Try last known location from any enabled provider — instant.
    val providers = try { locationManager.getProviders(true) } catch (e: Exception) { emptyList() }
    var best: Location? = null
    for (provider in providers) {
        val loc = try { locationManager.getLastKnownLocation(provider) } catch (e: SecurityException) { null }
        if (loc != null && (best == null || loc.accuracy < best!!.accuracy)) {
            best = loc
        }
    }
    if (best != null) return best

    // 2) No cached fix — ask for a single fresh update, with a timeout.
    val provider = when {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> return null   // no provider is even enabled
    }

    return try {
        suspendCoroutineWithTimeout(context, locationManager, provider)
    } catch (e: Exception) {
        null
    }
}

private suspend fun suspendCoroutineWithTimeout(
    context: Context,
    locationManager: LocationManager,
    provider: String
): Location? = kotlinx.coroutines.withTimeoutOrNull(8000L) {
    suspendCoroutine { continuation ->
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationManager.removeUpdates(this)
                continuation.resume(location)
            }
        }
        try {
            locationManager.requestLocationUpdates(
                provider,
                0L,
                0f,
                listener,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            continuation.resume(null)
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
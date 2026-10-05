package com.example.homiee.ui.screens.Residentflow

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.viewmodel.BookingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** One bookable service of the helper. [id] is what the create-booking API expects as service_id. */
data class ServiceOption(val id: Int, val name: String)

private val GreenPrimary  = Color(0xFF1A5C3A)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White
private val WarningRed    = Color(0xFFD32F2F)
private val BorderGray    = Color(0xFFCCCCCC)
private val ALL_HOURS = (1..12).map { it.toString() }

private const val SPECIAL_INSTRUCTIONS_LIMIT = 300

// Converts a 12-hour clock (hour + AM/PM) into minutes-since-midnight for comparison
private fun toMinutesOfDay(hour: String, period: String): Int {
    val h = hour.toIntOrNull() ?: 0
    val hour24 = when {
        period == "AM" && h == 12 -> 0        // 12 AM = midnight
        period == "PM" && h != 12 -> h + 12   // 1 PM–11 PM
        else                      -> h        // 12 PM stays 12, AM hours 1–11 stay as-is
    }
    return hour24 * 60
}

// "10" + "AM" -> "10:00:00" (the format the API expects)
private fun toApiTime(hour: String, period: String): String =
    String.format(Locale.ENGLISH, "%02d:00:00", toMinutesOfDay(hour, period) / 60)

// Midnight today, expressed in UTC millis — matches how Material3's DatePicker represents dates internally.
private fun todayUtcMidnightMillis(): Long {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private fun currentMinutesOfDay(): Int {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBookingScreen(
    helperId: Int,
    helperName: String,
    services: List<ServiceOption>,
    onBookingConfirmed: (bookingId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: BookingViewModel
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val submitError by viewModel.submitError.collectAsState()

    LaunchedEffect(Unit) { viewModel.clearSubmitError() }

    val todayMillis = remember { todayUtcMidnightMillis() }

    // ── Date picker: past dates are disabled in the UI itself ──
    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis >= todayMillis
            override fun isSelectableYear(year: Int): Boolean =
                year >= Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.YEAR)
        }
    }

    // ── All fields start empty; nothing is pre-filled ──
    var selectedService by remember { mutableStateOf<ServiceOption?>(null) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        selectableDates = selectableDates
    )
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDateMillis = datePickerState.selectedDateMillis

    // The picker returns UTC-midnight millis, so both formatters use UTC to avoid off-by-one days
    val displayDateFormat = remember {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
    }
    val apiDateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).apply { timeZone = TimeZone.getTimeZone("UTC") }
    }
    val selectedDateLabel = selectedDateMillis?.let { displayDateFormat.format(Date(it)) }
        ?: "Select a date"

    // Fallback safety-net check, in case a past date ever slips through
    val isDateInPast = selectedDateMillis != null && selectedDateMillis < todayMillis
    val isSelectedDateToday = selectedDateMillis != null && selectedDateMillis == todayMillis

    var startHour    by remember { mutableStateOf("") }
    var startPeriod  by remember { mutableStateOf("") }
    var endHour      by remember { mutableStateOf("") }
    var endPeriod    by remember { mutableStateOf("") }
    var specialInstructions by remember { mutableStateOf("") }

    // ── Time validation: only meaningful once every field is picked ──
    val allTimeFieldsFilled = startHour.isNotBlank() && startPeriod.isNotBlank() &&
            endHour.isNotBlank() && endPeriod.isNotBlank()
    val startMinutes = remember(startHour, startPeriod) { toMinutesOfDay(startHour, startPeriod) }
    val endMinutes   = remember(endHour, endPeriod)     { toMinutesOfDay(endHour, endPeriod) }
    val isTimeRangeInvalid = allTimeFieldsFilled && endMinutes <= startMinutes

    // If the booking is for today, neither start nor end time may already have passed
    val nowMinutes = currentMinutesOfDay()
    val isPastTimeInvalid = isSelectedDateToday && allTimeFieldsFilled &&
            (startMinutes < nowMinutes || endMinutes < nowMinutes)

    // ── GPS location + human-readable address ──
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var humanAddress by remember { mutableStateOf("") }
    var isFetchingLocation by remember { mutableStateOf(false) }
    var locationStatus by remember { mutableStateOf<String?>(null) }
    var locationStatusIsError by remember { mutableStateOf(false) }

    fun fetchAndReportLocation() {
        isFetchingLocation = true
        locationStatus = null
        coroutineScope.launch {
            val result = getCurrentLocationOrNull(context)
            if (result != null) {
                latitude = result.latitude.toString()
                longitude = result.longitude.toString()

                val readable = reverseGeocodeOrNull(context, result.latitude, result.longitude)
                humanAddress = readable
                    ?: "Lat ${"%.5f".format(result.latitude)}, Lng ${"%.5f".format(result.longitude)}"

                isFetchingLocation = false
                locationStatusIsError = false
                locationStatus = "Location detected successfully"
            } else {
                isFetchingLocation = false
                locationStatusIsError = true
                locationStatus = "Couldn't get your location. Make sure location services are on and try again."
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchAndReportLocation()
        } else {
            locationStatusIsError = true
            locationStatus = "Location permission is required to attach your location."
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
            fetchAndReportLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val isFormValid = selectedService != null &&
            selectedDateMillis != null &&
            !isDateInPast &&
            allTimeFieldsFilled &&
            !isTimeRangeInvalid &&
            !isPastTimeInvalid

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GreenDark)
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint               = Color.White,
                                modifier           = Modifier.size(16.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("New Booking", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(helperName, fontSize = 14.sp, color = Color.White.copy(alpha = 0.85f))
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

                    // ── Service: picked from the helper's own services (API needs service_id) ──
                    SectionLabel("SERVICE TYPE")
                    DropdownBox(
                        value       = selectedService?.name.orEmpty(),
                        options     = services.map { it.name },
                        placeholder = if (services.isEmpty()) "No services available" else "Select a service",
                        modifier    = Modifier.fillMaxWidth()
                    ) { name -> selectedService = services.firstOrNull { it.name == name } }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SELECT DATE")
                    Card(
                        shape     = RoundedCornerShape(14.dp),
                        colors    = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(
                                text       = selectedDateLabel,
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = if (selectedDateMillis != null) TextPrimary else TextSecondary
                            )
                            Icon(
                                imageVector        = Icons.Default.CalendarMonth,
                                contentDescription = "Pick date",
                                tint               = GreenPrimary
                            )
                        }
                    }

                    // Safety-net warning (shouldn't normally trigger since the picker disables past days)
                    if (isDateInPast) {
                        Spacer(Modifier.height(10.dp))
                        WarningBanner("Please enter a valid date (today or later)")
                    }

                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("OK", color = GreenPrimary, fontWeight = FontWeight.Bold)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Cancel", color = TextSecondary)
                                }
                            }
                        ) {
                            DatePicker(
                                state = datePickerState,
                                colors = DatePickerDefaults.colors(
                                    selectedDayContainerColor = GreenPrimary,
                                    todayDateBorderColor      = GreenPrimary,
                                    todayContentColor         = GreenPrimary
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SELECT TIME")

                    Text("Starting Time", fontSize = 16.sp, color = GreenDark)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(startHour, ALL_HOURS, "Hour", Modifier.weight(1f)) { startHour = it }
                        DropdownBox(startPeriod, listOf("AM", "PM"), "AM/PM", Modifier.weight(1f)) { startPeriod = it }
                    }

                    Spacer(Modifier.height(5.dp))
                    Text("Ending Time", fontSize = 16.sp, color = GreenDark)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(endHour, ALL_HOURS, "Hour", Modifier.weight(1f)) { endHour = it }
                        DropdownBox(endPeriod, listOf("AM", "PM"), "AM/PM", Modifier.weight(1f)) { endPeriod = it }
                    }

                    // ── Warning shown when the time range doesn't make sense ──
                    if (isTimeRangeInvalid) {
                        Spacer(Modifier.height(10.dp))
                        WarningBanner("Ending time must be after starting time")
                    }

                    // ── Warning shown when booking is for today but the picked time already passed ──
                    if (!isTimeRangeInvalid && isPastTimeInvalid) {
                        Spacer(Modifier.height(10.dp))
                        WarningBanner("Selected time has already passed today. Please choose a future time.")
                    }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SPECIAL INSTRUCTIONS")
                    OutlinedTextField(
                        value         = specialInstructions,
                        onValueChange = { newValue ->
                            // Enforce a hard 300-character cap
                            if (newValue.length <= SPECIAL_INSTRUCTIONS_LIMIT) {
                                specialInstructions = newValue
                            }
                        },
                        placeholder   = { Text("Any special instructions? (optional)", color = TextSecondary) },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape         = RoundedCornerShape(10.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedTextColor     = TextPrimary,
                            unfocusedTextColor   = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text      = "${specialInstructions.length}/$SPECIAL_INSTRUCTIONS_LIMIT",
                        fontSize  = 11.sp,
                        color     = TextSecondary,
                        modifier  = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SERVICE LOCATION")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGray, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, tint = GreenPrimary)
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Use Current Location (GPS)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                            Text("Attach your current location to this booking", fontSize = 12.sp, color = TextSecondary)
                        }
                        if (isFetchingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = GreenPrimary
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
                            color = if (locationStatusIsError) WarningRed else Color(0xFF16A34A),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                        )
                    }
                    // ── Human-readable address, shown once reverse geocoding succeeds ──
                    if (humanAddress.isNotBlank() && !locationStatusIsError) {
                        Text(
                            text = humanAddress,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    // ── Server error from the create-booking call ──
                    submitError?.let { message ->
                        Spacer(Modifier.height(16.dp))
                        WarningBanner(message)
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val dateMillis = selectedDateMillis
                            val service = selectedService
                            if (dateMillis != null && service != null) {
                                viewModel.createBooking(
                                    helperId            = helperId,
                                    serviceId           = service.id,
                                    bookingDate         = apiDateFormat.format(Date(dateMillis)),
                                    startTime           = toApiTime(startHour, startPeriod),
                                    endTime             = toApiTime(endHour, endPeriod),
                                    specialInstructions = specialInstructions,
                                    address             = humanAddress,
                                    onSuccess           = onBookingConfirmed
                                )
                            }
                        },
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        enabled  = isFormValid && !isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color       = Color.White
                            )
                        } else {
                            Text("Confirm Booking", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WarningBanner(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(WarningRed.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector        = Icons.Default.Warning,
            contentDescription = "Warning",
            tint               = WarningRed,
            modifier           = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text       = text,
            fontSize   = 12.sp,
            color      = WarningRed,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text          = text,
        fontSize      = 12.sp,
        fontWeight    = FontWeight.Bold,
        color         = TextPrimary,
        letterSpacing = 0.5.sp
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun DropdownBox(
    value: String,
    options: List<String>,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick  = { expanded = true },
            shape    = RoundedCornerShape(10.dp),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text       = value.ifBlank { placeholder },
                fontSize   = 14.sp,
                color      = if (value.isBlank()) TextSecondary else TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
            modifier         = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text     = option,
                            color    = TextPrimary,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = TextPrimary
                    )
                )
            }
        }
    }
}

/**
 * Tries getLastKnownLocation first (instant, no network wait). If nothing is cached,
 * falls back to a single fresh location request with a short timeout. Returns null if
 * location can't be determined (permission missing, providers disabled, or timeout).
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

    val providers = try { locationManager.getProviders(true) } catch (e: Exception) { emptyList() }
    var best: Location? = null
    for (provider in providers) {
        val loc = try { locationManager.getLastKnownLocation(provider) } catch (e: SecurityException) { null }
        if (loc != null && (best == null || loc.accuracy < best!!.accuracy)) {
            best = loc
        }
    }
    if (best != null) return best

    val provider = when {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> return null
    }

    return try {
        requestSingleLocationUpdate(locationManager, provider)
    } catch (e: Exception) {
        null
    }
}

private suspend fun requestSingleLocationUpdate(
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

/**
 * Turns raw coordinates into a human-readable address (e.g. "12 MG Road, Lucknow, UP 226001").
 * Runs on IO dispatcher since Geocoder can block on network/disk. Returns null on any failure
 * (no network, no geocoder backend on device, etc.) so the caller can fall back to raw lat/lng.
 */
private suspend fun reverseGeocodeOrNull(context: Context, lat: Double, lng: Double): String? =
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val results = geocoder.getFromLocation(lat, lng, 1)
            results?.firstOrNull()?.let { addr ->
                addr.getAddressLine(0)
                    ?: listOfNotNull(addr.subLocality, addr.locality, addr.adminArea, addr.postalCode)
                        .joinToString(", ")
                        .ifBlank { null }
            }
        } catch (e: Exception) {
            null
        }
    }
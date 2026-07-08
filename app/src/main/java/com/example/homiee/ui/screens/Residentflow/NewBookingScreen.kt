package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.viewmodel.BookingViewModel
import com.example.homiee.viewmodel.BookingViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

private val GreenPrimary  = Color(0xFF1A5C3A)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White
private val WarningRed    = Color(0xFFD32F2F)
private val ALL_HOURS = (1..12).map { it.toString() }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBookingScreen(
    helperName: String,
    helperService: String,
    helperRating: Float,
    onBookingConfirmed: (bookingId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(LocalContext.current)
    )
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var serviceType by remember { mutableStateOf(helperService) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDateMillis = datePickerState.selectedDateMillis

    val displayDateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }
    val selectedDateLabel = selectedDateMillis?.let { displayDateFormat.format(Date(it)) }
        ?: "Select a date"

    var startHour    by remember { mutableStateOf("10") }
    var startPeriod  by remember { mutableStateOf("AM") }
    var endHour      by remember { mutableStateOf("12") }
    var endPeriod    by remember { mutableStateOf("PM") }
    var specialInstructions by remember { mutableStateOf("") }

    // ── Time validation: end must be strictly after start ──
    val startMinutes = remember(startHour, startPeriod) { toMinutesOfDay(startHour, startPeriod) }
    val endMinutes    = remember(endHour, endPeriod)    { toMinutesOfDay(endHour, endPeriod) }
    val isTimeRangeInvalid = endMinutes <= startMinutes

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
                            Text("NEW BOOKING", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(helperName, fontSize = 14.sp, color = Color.White.copy(alpha = 0.85f))
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

                    SectionLabel("SERVICE TYPE")
                    OutlinedTextField(
                        value           = serviceType,
                        onValueChange   = { serviceType = it },
                        modifier        = Modifier.fillMaxWidth(),
                        shape           = RoundedCornerShape(10.dp),
                        colors          = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )

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
                                color      = TextPrimary
                            )
                            Icon(
                                imageVector        = Icons.Default.CalendarMonth,
                                contentDescription = "Pick date",
                                tint               = GreenPrimary
                            )
                        }
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

                    Text("Starting Time", fontSize = 12.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(startHour, ALL_HOURS, Modifier.weight(1f)) { startHour = it }
                        DropdownBox(startPeriod, listOf("AM", "PM"), Modifier.weight(1f)) { startPeriod = it }
                    }

                    Spacer(Modifier.height(14.dp))
                    Text("Ending Time", fontSize = 12.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(endHour, ALL_HOURS, Modifier.weight(1f)) { endHour = it }
                        DropdownBox(endPeriod, listOf("AM", "PM"), Modifier.weight(1f)) { endPeriod = it }
                    }

                    // ── Warning shown when the time range doesn't make sense ──
                    if (isTimeRangeInvalid) {
                        Spacer(Modifier.height(10.dp))
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
                                text     = "Ending time must be after starting time",
                                fontSize = 12.sp,
                                color    = WarningRed,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SPECIAL INSTRUCTIONS")
                    OutlinedTextField(
                        value         = specialInstructions,
                        onValueChange = { specialInstructions = it },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape         = RoundedCornerShape(10.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFCCCCCC)
                        )
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val bookingDateLabel = selectedDateMillis?.let { displayDateFormat.format(Date(it)) }
                                ?: "Not selected"
                            val id = viewModel.createBooking(
                                helperName = helperName,
                                service    = serviceType,
                                rating     = helperRating,
                                date       = bookingDateLabel,
                                time       = "$startHour:00 $startPeriod"
                            )
                            onBookingConfirmed(id)
                        },
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        enabled  = selectedDateMillis != null && !isTimeRangeInvalid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Confirm Booking", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
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
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick  = { expanded = true },
            shape    = RoundedCornerShape(10.dp),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(value, fontSize = 14.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onSelect(option)
                    expanded = false
                })
            }
        }
    }
}
package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.R
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.viewmodel.BookingViewModel
import com.example.homiee.viewmodel.BookingViewModelFactory

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White

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
    var selectedDay  by remember { mutableStateOf(12) }
    var startHour    by remember { mutableStateOf("10") }
    var startPeriod  by remember { mutableStateOf("AM") }
    var endHour      by remember { mutableStateOf("12") }
    var endPeriod    by remember { mutableStateOf("PM") }
    var specialInstructions by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter            = painterResource(R.drawable.bg),
            contentDescription = null,
            contentScale       = ContentScale.FillBounds,
            modifier           = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GreenPrimary)
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
                        modifier  = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("June 2026", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                            Spacer(Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                                    Text(it, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(32.dp))
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            val weeks = listOf(
                                listOf(null, 1, 2, 3, 4, 5, 6),
                                listOf(7, 8, 9, 10, 11, 12, 13),
                                listOf(14, 15, 16, 17, 18, 19, 20),
                                listOf(21, 22, 23, 24, 25, 26, 27),
                                listOf(28, 29, 30, null, null, null, null)
                            )
                            weeks.forEach { week ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    week.forEach { day ->
                                        Box(
                                            modifier = Modifier
                                                .width(32.dp)
                                                .height(32.dp)
                                                .clip(CircleShape)
                                                .background(if (day == selectedDay) GreenPrimary else Color.Transparent)
                                                .clickable(enabled = day != null) { if (day != null) selectedDay = day },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (day != null) {
                                                Text(
                                                    day.toString(),
                                                    fontSize = 13.sp,
                                                    color = if (day == selectedDay) Color.White else TextPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    SectionLabel("SELECT TIME")

                    Text("Starting Time", fontSize = 12.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(startHour, listOf("9", "10", "11"), Modifier.weight(1f)) { startHour = it }
                        DropdownBox(startPeriod, listOf("AM", "PM"), Modifier.weight(1f)) { startPeriod = it }
                    }

                    Spacer(Modifier.height(14.dp))
                    Text("Ending Time", fontSize = 12.sp, color = TextSecondary)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DropdownBox(endHour, listOf("11", "12", "1"), Modifier.weight(1f)) { endHour = it }
                        DropdownBox(endPeriod, listOf("AM", "PM"), Modifier.weight(1f)) { endPeriod = it }
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
                            val id = viewModel.createBooking(
                                helperName = helperName,
                                service    = serviceType,
                                rating     = helperRating,
                                date       = "Jun $selectedDay, 2026",
                                time       = "$startHour:00 $startPeriod"
                            )
                            onBookingConfirmed(id)
                        },
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
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
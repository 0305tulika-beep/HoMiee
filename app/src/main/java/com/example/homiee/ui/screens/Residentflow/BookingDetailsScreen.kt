package com.example.homiee.ui.screens.Residentflow

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.viewmodel.BookingViewModel


private val GreenLight    = Color(0xFFE8F5EE)
private val GreenPrimary  = Color(0xFF1A5C3A)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White

@Composable
fun BookingDetailsScreen(
    bookingId: String,
    viewModel: BookingViewModel,
    onChat:    (BookingItem) -> Unit = {},
    onBack:    () -> Unit
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val context = LocalContext.current

    val state        by viewModel.detail.collectAsState()
    val isCancelling by viewModel.isCancelling.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    // Only trust the state if it belongs to this booking (it may still hold the previous one)
    val booking = state.booking?.takeIf { it.id == bookingId }

    LaunchedEffect(bookingId) { viewModel.loadDetail(bookingId) }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    if (showCancelDialog) {
        CancelBookingDialog(
            isCancelling = isCancelling,
            onConfirm    = {
                viewModel.cancelBooking(bookingId) { success ->
                    showCancelDialog = false
                    if (success) onBack()
                }
            },
            onDismiss    = { showCancelDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header: back arrow + title — outside the card, top of screen ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GreenLight)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint               = GreenPrimary,
                        modifier           = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    text       = "Booking Details",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary
                )
            }
            Spacer(Modifier.height(4.dp))

            when {
                // ── Nothing to show yet ──────────────────────────────────────
                booking == null && state.errorMessage == null -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenDark)
                    }
                }

                // ── Failed and no cached copy ────────────────────────────────
                booking == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 24.dp, end = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text     = state.errorMessage ?: "",
                            color    = TextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        TextButton(onClick = { viewModel.loadDetail(bookingId) }) {
                            Text("Retry", color = GreenDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // ── Content ──────────────────────────────────────────────────
                else -> {
                    val canCancel = booking.rawStatus == "pending" || booking.rawStatus == "confirmed"

                    LazyColumn(
                        modifier       = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            DetailsSectionCard(title = "BOOKING INFORMATION") {
                                DetailRow("Helper's name", booking.helperName)
                                DetailRow("Booking ID", "#${booking.id}")
                                DetailRow("Service", booking.service)
                                DetailRow("Status", booking.rawStatus.replaceFirstChar { it.uppercase() })
                                if (booking.durationLabel.isNotBlank()) {
                                    DetailRow("Duration", booking.durationLabel)
                                }
                                DetailRow("Date", booking.bookingDate)
                                DetailRow(
                                    "Time",
                                    if (booking.endTime.isNotBlank()) "${booking.bookingTime} – ${booking.endTime}"
                                    else booking.bookingTime
                                )
                                if (booking.totalAmount.isNotBlank()) {
                                    DetailRow("Total amount", "₹${booking.totalAmount}")
                                }
                            }
                        }

                        // ── Special instructions — only if the resident wrote any ──
                        if (booking.specialInstructions.isNotBlank()) {
                            item {
                                DetailsSectionCard(title = "SPECIAL INSTRUCTIONS") {
                                    Text(
                                        text     = booking.specialInstructions,
                                        fontSize = 13.sp,
                                        color    = TextPrimary
                                    )
                                }
                            }
                        }

                        // ── Service location — only shown if an address was actually captured ──
                        if (booking.address.isNotBlank()) {
                            item {
                                DetailsSectionCard(title = "SERVICE LOCATION") {
                                    Text(
                                        text       = booking.address,
                                        fontSize   = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color      = TextPrimary
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier              = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick  = { onChat(booking) },
                                    shape    = RoundedCornerShape(12.dp),
                                    border   = BorderStroke(1.5.dp, GreenPrimary),
                                    colors   = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor   = GreenPrimary
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                ) {
                                    Text(
                                        text       = "Chat",
                                        color      = GreenPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize   = 15.sp
                                    )
                                }

                                // Only pending / confirmed bookings can be cancelled
                                if (canCancel) {
                                    Button(
                                        onClick        = { showCancelDialog = true },
                                        shape          = RoundedCornerShape(12.dp),
                                        colors         = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFD32F2F)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp),
                                        modifier       = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                    ) {
                                        Text(
                                            text       = "Cancel",
                                            color      = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize   = 15.sp,
                                            maxLines   = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailsSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenPrimary, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}
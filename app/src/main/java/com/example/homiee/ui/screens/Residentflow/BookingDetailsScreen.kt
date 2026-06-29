package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import androidx.compose.foundation.border
import com.example.homiee.ui.theme.GreenDark

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White

@Composable
fun BookingDetailsScreen(
    bookingId:     String,
    helperName:    String,
    service:       String,
    bookingDate:   String,
    bookingTime:   String,
    durationHours: String  = "2 hours",
    onChat:        () -> Unit = {},
    onCancel:      () -> Unit = {},
    onBack:        () -> Unit
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Box(modifier = Modifier.fillMaxSize()) {

        androidx.compose.foundation.layout.Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ──
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
                        Text("Booking Details", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("#$bookingId", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
            Spacer(Modifier.height(100.dp))
            androidx.compose.foundation.lazy.LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    DetailsSectionCard(title = "BOOKING INFORMATION") {
                        DetailRow("Service", service)
                        DetailRow("Duration", durationHours)
                        DetailRow("Date", bookingDate)
                        DetailRow("Time", bookingTime)
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
                        // ── Chat button — text color set explicitly ──
                        OutlinedButton(
                            onClick  = onChat,
                            shape    = RoundedCornerShape(12.dp),
                            border   = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary),
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

                        // ── Cancel button — shortened text to fit ──
                        Button(
                            onClick  = onCancel,
                            shape    = RoundedCornerShape(12.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD32F2F)
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier
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

@Composable
private fun DetailsSectionCard(title: String, content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
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
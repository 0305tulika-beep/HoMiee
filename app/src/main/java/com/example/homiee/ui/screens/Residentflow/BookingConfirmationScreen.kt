package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenCircle   = Color(0xFFE8F5EE)
private val TextSecondary = Color(0xFF7A7A7A)
private val TextDark      = Color(0xFF1A1A1A)

/**
 * Shown when a helper has accepted the resident's booking request.
 * It stays on screen until the resident taps a button: nothing happens automatically.
 */
@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    helperName: String,
    bookingDate: String,
    bookingTime: String,
    totalAmount: String = "",
    onViewHelperDetails: () -> Unit,
    onBackToHome: () -> Unit
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp)
        ) {

            AnimatedStatusIcon(
                isSuccess = true,
                accent = GreenPrimary,
                soft = GreenCircle
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Booking Confirmed!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "$helperName has accepted your booking request",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(28.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    ConfirmationRow("Booking ID", "#$bookingId")
                    ConfirmationRow("Helper", helperName)
                    ConfirmationRow("Date", bookingDate)
                    ConfirmationRow("Time", bookingTime)
                    if (totalAmount.isNotBlank()) ConfirmationRow("Total Amount", totalAmount)
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onViewHelperDetails,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "View helper's details",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))

            TextButton(onClick = onBackToHome) {
                Text(
                    "Back to home",
                    color = GreenPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ConfirmationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
    }
}
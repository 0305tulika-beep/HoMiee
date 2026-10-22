package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark

private val DeclinedRed        = Color(0xFFE6483A)
private val DeclinedRedCircle  = Color(0xFFFDEDEC)
private val DeclinedTextMuted  = Color(0xFF7A7A7A)
private val DeclinedTextDark   = Color(0xFF1A1A1A)

/**
 * Shown when a helper declines the resident's booking request.
 * It does NOT auto-dismiss: the resident has to choose what to do next.
 */
@Composable
fun BookingDeclinedScreen(
    bookingId: String,
    helperName: String,
    service: String,
    bookingDate: String,
    bookingTime: String,
    onFindAnotherHelper: () -> Unit,
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
                isSuccess = false,
                accent = DeclinedRed,
                soft = DeclinedRedCircle
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Request Declined",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DeclinedTextDark
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "$helperName isn't able to take this booking. You haven't been charged.",
                fontSize = 13.sp,
                color = DeclinedTextMuted,
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
                    DeclinedRow("Booking ID", "#$bookingId")
                    DeclinedRow("Helper", helperName)
                    if (service.isNotBlank()) DeclinedRow("Service", service)
                    if (bookingDate.isNotBlank()) DeclinedRow("Date", bookingDate)
                    if (bookingTime.isNotBlank()) DeclinedRow("Time", bookingTime)
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onFindAnotherHelper,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Find another helper",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))

            TextButton(onClick = onBackToHome) {
                Text(
                    "Back to home",
                    color = GreenDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DeclinedRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = DeclinedTextMuted)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DeclinedTextDark)
    }
}
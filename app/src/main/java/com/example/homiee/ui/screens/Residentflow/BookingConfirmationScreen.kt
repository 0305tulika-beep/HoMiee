package com.example.homiee.ui.screens.Residentflow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import kotlinx.coroutines.delay

private val GreenPrimary = Color(0xFF1A5C3A)
private val GreenCircle  = Color(0xFFE8F5EE)
private val TextSecondary = Color(0xFF7A7A7A)

@Composable
fun BookingConfirmationScreen(
    bookingId: String,
    helperName: String,
    bookingDate: String,
    bookingTime: String,
    totalAmount: String = "2000",
    onTimeout: () -> Unit
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .background(GreenCircle),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Check,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Booking Confirmed!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Spacer(Modifier.height(6.dp))
            Text(
                "We've Notified $helperName About Your Request",
                fontSize  = 13.sp,
                color     = TextSecondary,
                modifier  = Modifier.padding(horizontal = 40.dp)
            )

            Spacer(Modifier.height(28.dp))

            Card(
                shape     = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier  = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    ConfirmationRow("Booking ID", "#$bookingId")
                    ConfirmationRow("Helper", helperName)
                    ConfirmationRow("Date", bookingDate)
                    ConfirmationRow("Time", bookingTime)
                    ConfirmationRow("Total Amount", totalAmount)
                }
            }
        }
    }
}

@Composable
private fun ConfirmationRow(label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1A1A))
    }
}
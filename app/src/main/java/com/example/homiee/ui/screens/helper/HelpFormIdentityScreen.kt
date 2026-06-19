package com.example.homiee.ui.screens.helper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.WarningAmber

@Composable
fun HelpFormIdentityScreen(onNext: () -> Unit) {
    val verificationStatus = "pending"

    HelpFormShell(title = "Identity Verification", step = 1, onNext = onNext) {

        // Info banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenTint, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(
                text     = "Your documents are encrypted and handled per India's data protection laws. " +
                        "Reviewed within 24 hours.",
                color    = GreenDark,
                fontSize = 13.sp
            )
        }

        // ── Button 1: Upload Government ID ──────────────────────────────
        Surface(
            onClick  = { /* TODO: launch file picker */ },
            shape    = RoundedCornerShape(12.dp),
            color    = GreenTint,
            border   = BorderStroke(1.5.dp, GreenMid),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier              = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.Description,
                    contentDescription = null,
                    tint               = GreenMid,
                    modifier           = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text       = "Upload Any Valid Government\nID Proof",
                    fontWeight = FontWeight.Medium,
                    fontSize   = 15.sp,
                    lineHeight = 20.sp,
                    color      = GreenMid
                )
            }
        }

        // ── Button 2: Selfie / Live verification ────────────────────────
        Surface(
            onClick  = { /* TODO: open camera */ },
            shape    = RoundedCornerShape(12.dp),
            color    = GreenTint,
            border   = BorderStroke(1.5.dp, GreenMid),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier              = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint               = GreenMid,
                    modifier           = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text       = "Take a selfie for live verification",
                    fontWeight = FontWeight.Medium,
                    fontSize   = 15.sp,
                    color      = GreenMid
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0))

        Text(
            "VERIFICATION STATUS",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        val (statusColor, statusText) = when (verificationStatus) {
            "approved" -> Color(0xFF2E7D32) to "✓ Identity Verified"
            "rejected" -> Color(0xFFD32F2F) to "✗ Verification Failed — re-upload"
            else       -> WarningAmber      to "⏳ Pending review after submission"
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(statusColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint     = statusColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(statusText, color = statusColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
        }
    }
}
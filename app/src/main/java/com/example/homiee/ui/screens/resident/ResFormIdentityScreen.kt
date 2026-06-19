package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.CardTextField
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextPrimary

@Composable
fun ResFormIdentityScreen(onNext: () -> Unit) {
    var emergencyName   by remember { mutableStateOf("") }
    var emergencyMobile by remember { mutableStateOf("") }

    ResFormShell(title = "Safety & Identity", step = 4, onNext = onNext) {

        Text(
            "EMERGENCY CONTACT",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        CardTextField(emergencyName, { emergencyName = it }, "Enter name", label = "NAME")
        CardTextField(
            value         = emergencyMobile,
            onValueChange = { emergencyMobile = it },
            placeholder   = "Enter mobile",
            label         = "MOBILE NO.",
            keyboardType  = KeyboardType.Phone
        )

        Divider(color = Color(0xFFE0E0E0))

        Text(
            "IDENTITY VERIFICATION",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        // ── Button 1: Upload Government ID ──────────────────────────────
        Surface(
            onClick  = { /* TODO: launch file picker for Government ID */ },
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
            onClick  = { /* TODO: launch camera for selfie */ },
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
    }
}
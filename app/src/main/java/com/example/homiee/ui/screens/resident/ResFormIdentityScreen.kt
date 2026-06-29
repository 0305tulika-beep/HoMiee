package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextPrimary

@Composable
fun ResFormIdentityScreen(onNext: () -> Unit) {

    ResFormShell(title = "Identity Verification", step = 3, onNext = onNext) {

        Text(
            "IDENTITY VERIFICATION",
            fontWeight    = FontWeight.Bold,
            fontSize      = 13.sp,
            color         = TextPrimary,
            letterSpacing = 0.5.sp
        )

        UploadButton(label = "Upload Aadhaar card") {
            /* TODO: launch file picker for Aadhaar */
        }

        UploadButton(label = "Upload PAN (Optional)") {
            /* TODO: launch file picker for PAN */
        }
    }
}

@Composable
private fun UploadButton(label: String, onClick: () -> Unit) {
    Surface(
        onClick  = onClick,
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
                text       = label,
                fontWeight = FontWeight.Medium,
                fontSize   = 15.sp,
                color      = GreenMid
            )
        }
    }
}
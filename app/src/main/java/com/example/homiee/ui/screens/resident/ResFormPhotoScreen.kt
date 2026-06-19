package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextMuted

@Composable
fun ResFormPhotoScreen(onFinish: () -> Unit) {
ResFormShell(title = "Profile Photo", step = 5, onNext = onFinish, nextLabel = "Finish") {
Box(
contentAlignment = Alignment.Center,
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 40.dp)
) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Box(
contentAlignment = Alignment.Center,
modifier = Modifier
.size(120.dp)
.clip(CircleShape)
.background(GreenTint)
.border(2.dp, GreenLight, CircleShape)
.clickable { /* TODO: launch image picker */ }
) {
Icon(
Icons.Default.CameraAlt,
contentDescription = null,
tint     = GreenDark,
modifier = Modifier.size(40.dp)
)
}

Spacer(Modifier.height(16.dp))

Text("Tap To Upload", color = GreenMid, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
Text("JPG or PNG, up to 5MB", color = TextMuted, fontSize = 13.sp)
}
}
}
}
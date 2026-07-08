package com.example.homiee.ui.screens.resident

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.homiee.ui.components.HomieeColors

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadiusDp: Int = 20,
    strokeWidthDp: Int = 2,
    dashLengthDp: Int = 10,
    gapLengthDp: Int = 8
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidthDp.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLengthDp.dp.toPx(), gapLengthDp.dp.toPx()), 0f
        )
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadiusDp.dp.toPx(), cornerRadiusDp.dp.toPx()),
        size = Size(size.width, size.height)
    )
}


@Composable
fun ResFormPhotoScreen(
    onFinish: () -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    OnboardingStepScaffold(
        currentStep = 4,
        title = "Profile Photo",
        subtitle = "Upload a clear photo for your profile.",
        buttonText = "Finish",
        buttonIcon = Icons.Default.Check,
        onButtonClick = onFinish
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(20.dp))
                .dashedBorder(color = HomieeColors.PrimaryDark.copy(alpha = 0.5f))
                .clickable { pickImageLauncher.launch("image/*") }
        ) {
            if (imageUri != null) {
                // Selected photo fills the drop-zone
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                )

                // Small edit badge, bottom-right, to re-pick the photo
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HomieeColors.PrimaryDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change photo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                // Empty state: camera icon + prompts
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(HomieeColors.PrimaryMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = HomieeColors.PrimaryDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Tap to Upload",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "JPG or PNG, up to 5MB",
                        fontSize = 12.sp,
                        color = HomieeColors.TextGray
                    )
                }
            }
        }
    }
}
package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val CardBg        = Color.White
private val VerifiedColor = Color(0xFF2ECC71)

/**
 * Generic full-screen viewer for a single verified document.
 * The actual image/PDF URL should come from the helper's profile data
 * on the backend (e.g. helper.governmentIdUrl / helper.policeVerificationUrl).
 */
@Composable
fun DocumentViewerScreen(
    documentType: VerifiedDocumentType,
    documentImageUrl: String? = null,
    isVerified: Boolean = true,
    onBack: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    val (title, subtitle) = when (documentType) {
        VerifiedDocumentType.GOVERNMENT_ID ->
            "Government ID" to "Aadhaar Card"
        VerifiedDocumentType.POLICE_VERIFICATION ->
            "Police Verification Certificate" to "Issued by Lucknow Police"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9F8))
    ) {

        // ── Top bar ─────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GreenLight)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = GreenPrimary,
                    modifier           = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text       = title,
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // ── Document preview card ──────────────────────────────────
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (documentImageUrl != null) {
                        // Replace with Coil's AsyncImage in the real app, e.g.:
                        // AsyncImage(model = documentImageUrl, contentDescription = title,
                        //     contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
                        Image(
                            painter            = painterResource(R.drawable.ic_profile_placeholder),
                            contentDescription = title,
                            contentScale       = ContentScale.Fit,
                            modifier           = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    } else {
                        Text(
                            text     = "Document preview unavailable",
                            fontSize = 13.sp,
                            color    = TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Details row ─────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = title,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = subtitle,
                        fontSize = 12.sp,
                        color    = TextSecondary
                    )
                }
                if (isVerified) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(VerifiedColor.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint               = VerifiedColor,
                            modifier           = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text       = "Verified",
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = VerifiedColor
                        )
                    }
                }
            }
        }
    }
}
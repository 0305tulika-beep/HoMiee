package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.VerifiedUser
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

private val GreenPrimary   = Color(0xFF1A5C3A)
private val GreenLight     = Color(0xFFE8F5EE)
private val GreenText      = Color(0xFF1A5C3A)
private val TextPrimary    = Color(0xFF1A1A1A)
private val TextSecondary  = Color(0xFF7A7A7A)
private val CardBg         = Color.White
private val VerifiedColor  = Color(0xFF2ECC71)

/** Represents a single verifiable document type shown in this list. */
enum class VerifiedDocumentType {
    GOVERNMENT_ID,
    POLICE_VERIFICATION
}

data class VerifiedDocument(
    val type: VerifiedDocumentType,
    val title: String,
    val subtitle: String,
    val isVerified: Boolean
)

private val MOCK_DOCUMENTS = listOf(
    VerifiedDocument(
        type       = VerifiedDocumentType.GOVERNMENT_ID,
        title      = "Government ID",
        subtitle   = "Aadhaar Card",
        isVerified = true
    ),
    VerifiedDocument(
        type       = VerifiedDocumentType.POLICE_VERIFICATION,
        title      = "Police Verification Certificate",
        subtitle   = "Issued by Lucknow Police",
        isVerified = true
    )
)

@Composable
fun VerifiedDocumentsScreen(
    onBack: () -> Unit = {},
    onDocumentClick: (VerifiedDocumentType) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

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
                text       = "Verified Documents",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MOCK_DOCUMENTS.forEach { document ->
                DocumentRow(
                    document = document,
                    onClick  = { onDocumentClick(document.type) }
                )
            }
        }
    }
}

@Composable
private fun DocumentRow(document: VerifiedDocument, onClick: () -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(GreenLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Description,
                        contentDescription = null,
                        tint               = GreenPrimary,
                        modifier           = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = document.title,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text     = document.subtitle,
                        fontSize = 12.sp,
                        color    = TextSecondary
                    )
                    if (document.isVerified) {
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector        = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint               = VerifiedColor,
                                modifier           = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text       = "Verified",
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color      = VerifiedColor
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector        = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint               = TextSecondary
            )
        }
    }
}
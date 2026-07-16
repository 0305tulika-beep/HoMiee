package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
private val StarColor     = Color(0xFFF4B400)
private val CardBg        = Color.White

@Composable
fun HelperReviewsScreen(
    helperId: String = "",
    onBack: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Header ─────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                text       = "Reviews",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
        }

        HorizontalDivider(color = Color(0xFFF0F0F0))

        // ── Summary ────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter            = painterResource(R.drawable.star),
                contentDescription = null,
                tint               = StarColor,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = String.format("%.1f", MOCK_HELPER.rating),
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text     = "based on ${MOCK_HELPER.reviewCount} reviews",
                fontSize = 13.sp,
                color    = TextSecondary
            )
        }

        // ── Reviews list ───────────────────────────────────
        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MOCK_HELPER.reviews) { review ->
                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(14.dp),
                    colors    = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    review.reviewerName,
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = TextPrimary
                                )
                                Row {
                                    repeat(review.rating) {
                                        Icon(painterResource(R.drawable.star), null, tint = StarColor,        modifier = Modifier.size(12.dp))
                                    }
                                    repeat(5 - review.rating) {
                                        Icon(painterResource(R.drawable.star), null, tint = Color(0xFFDDDDDD), modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                            Text(review.timeAgo, fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(review.comment, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}
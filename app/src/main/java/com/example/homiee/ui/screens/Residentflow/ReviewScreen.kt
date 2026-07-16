package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)

data class Review(
    val reviewerName: String,
    val timeAgo:      String,
    val rating:       Int,
    val comment:      String,
    val tags:         List<String> = emptyList()
)

val reviews = listOf(
    Review(
        reviewerName = "Priya S.",
        timeAgo      = "2 days ago",
        rating       = 4,
        comment      = "Great experience, very professional and punctual.",
        tags         = listOf("Professional", "Punctual")
    ),
    Review(
        reviewerName = "Arun M.",
        timeAgo      = "1 week ago",
        rating       = 4,
        comment      = "Very good work, would recommend to others.",
        tags         = listOf("Friendly", "Skilled")
    ),
    Review(
        reviewerName = "Priya S.",
        timeAgo      = "2 days ago",
        rating       = 3,
        comment      = "Decent work but could improve on timing.",
    ),
    Review(
        reviewerName = "Arun M.",
        timeAgo      = "1 week ago",
        rating       = 4,
        comment      = "Excellent cooking skills, very happy with the service.",
        tags         = listOf("Professional", "Skilled", "Friendly")
    ),
)

@Composable
fun MyReviewsScreen(
    onBack: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header: back arrow + title — outside the card, top of screen ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GreenLight)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint               = GreenPrimary,
                        modifier           = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    text       = "My Reviews",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = com.example.homiee.ui.screens.Residentflow.TextPrimary
                )
            }

            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding      = PaddingValues(bottom = 24.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(review.reviewerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(review.timeAgo,      fontSize   = 11.sp, color = TextMuted)
            }
            Spacer(Modifier.height(4.dp))
            Row {
                repeat(5) { index ->
                    Text(
                        text  = if (index < review.rating) "★" else "☆",
                        color = if (index < review.rating) Color(0xFFFFC107) else TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
            if (review.tags.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement   = Arrangement.spacedBy(6.dp)
                ) {
                    review.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(GreenDark.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text       = tag,
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color      = GreenDark
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(review.comment, fontSize = 12.sp, color = TextMuted, lineHeight = 18.sp)
        }
    }
}
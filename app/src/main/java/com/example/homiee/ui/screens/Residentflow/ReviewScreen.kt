package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White

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

        Image(
            painter            = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header with back ──
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(systemBarsPadding())
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint               = White
                    )
                }
                Text(
                    text       = "My Reviews",
                    fontSize   = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color      = White
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
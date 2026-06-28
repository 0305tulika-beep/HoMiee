package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
private val GreenText     = Color(0xFF1A5C3A)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val StarColor     = Color(0xFFF4B400)
private val CardBg        = Color.White

data class HelperReview(
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)

data class ServiceWithPrice(val name: String, val price: String)

private val MOCK_HELPER = object {
    val name              = "Ramesh Kumar"
    val distance          = "0.8 km away"
    val rating            = 4.9f
    val about             = "Experienced and trustworthy home helper with 5 years of experience working with families across Lucknow. Speaks Hindi and English. Background verified and highly rated."
    val services          = listOf(
        ServiceWithPrice("Cooking",  "₹250/hr"),
        ServiceWithPrice("Cleaning", "₹200/hr"),
        ServiceWithPrice("Laundry",  "₹150/hr")
    )
    val experience        = "5 years"
    val languages         = "Hindi, English"
    val availability      = listOf("Morning", "Afternoon", "Evening")
    val phoneVerified     = true
    val idVerified        = true
    val backgroundChecked = true
    val reviews           = listOf(
        HelperReview("Priya S.", 4, "Very punctual and thorough with cleaning. Would book again.", "2 days ago"),
        HelperReview("Arun M.",  4, "Cooked amazing food. My family loved it.", "1 week ago"),
    )
    val reviewCount = 22
}

@Composable
fun HelperProfileScreen(
    helperId: String = "",
    onBookNow: (String) -> Unit = {},
    onBack: () -> Unit = {}             // ← already existed, now actually used
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter            = painterResource(R.drawable.bg),
            contentDescription = null,
            contentScale       = ContentScale.FillBounds,
            modifier           = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {

            // ── Hero card: photo + name + about merged ──────────────────
            item {
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = CardBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // ── Back arrow row ─────────────────────────────
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier          = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GreenLight)
                                    .clickable { onBack() },    // ← calls popBackStack in Navgraph
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
                                text       = "Helper Profile",
                                fontSize   = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = TextPrimary
                            )
                        }

                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Spacer(Modifier.height(14.dp))

                        // Photo + name + distance + rating
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier         = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(GreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter            = painterResource(R.drawable.ic_profile_placeholder),
                                    contentDescription = "Helper",
                                    contentScale       = ContentScale.Crop,
                                    modifier           = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(
                                    text       = MOCK_HELPER.name,
                                    fontSize   = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary
                                )
                                Text(
                                    text     = MOCK_HELPER.distance,
                                    fontSize = 12.sp,
                                    color    = TextSecondary
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter            = painterResource(R.drawable.star),
                                        contentDescription = null,
                                        tint               = StarColor,
                                        modifier           = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text     = String.format("%.1f", MOCK_HELPER.rating),
                                        fontSize = 13.sp,
                                        color    = TextSecondary
                                    )
                                }
                            }
                        }

                        // About text merged directly below
                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text          = "About",
                            fontSize      = 13.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = GreenPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text       = MOCK_HELPER.about,
                            fontSize   = 13.sp,
                            color      = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // ── Services with prices ─────────────────────────────────────
            item {
                SectionCard(title = "SERVICES OFFERED") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        MOCK_HELPER.services.forEach { service ->
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(GreenLight)
                                        .border(1.dp, GreenPrimary, RoundedCornerShape(20.dp))
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text       = service.name,
                                        fontSize   = 13.sp,
                                        color      = GreenText,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text       = service.price,
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // ── Experience + Languages ───────────────────────────────────
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoBox(label = "EXPERIENCE", value = MOCK_HELPER.experience, modifier = Modifier.weight(1f))
                    InfoBox(label = "LANGUAGES",  value = MOCK_HELPER.languages,  modifier = Modifier.weight(1f))
                }
            }

            // ── Availability ─────────────────────────────────────────────
            item {
                SectionCard(title = "AVAILABILITY") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MOCK_HELPER.availability.forEach { slot ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(GreenLight)
                                    .border(1.dp, GreenPrimary, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(slot, fontSize = 13.sp, color = GreenText, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // ── Reviews ──────────────────────────────────────────────────
            item {
                SectionCard(title = "REVIEWS (${MOCK_HELPER.reviewCount})") {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        MOCK_HELPER.reviews.forEach { review ->
                            Column {
                                Row(
                                    modifier              = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment     = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(review.reviewerName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
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
                                Spacer(Modifier.height(4.dp))
                                Text(review.comment, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier              = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            // Chat button
            OutlinedButton(
                onClick  = { /* TODO: chat */ },
                shape    = RoundedCornerShape(14.dp),
                border   = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary),
                colors   = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor   = GreenPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Chat", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }

            // Book Now button
            Button(
                onClick  = { onBookNow(helperId) },
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Book Now", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenPrimary, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun InfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier.padding(vertical = 6.dp),
        shape     = RoundedCornerShape(10.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, fontSize = 10.sp, color = TextSecondary, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}
package com.example.homiee.ui.screens.Residentflow

import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import java.util.Locale

private val GreenPrimary   = Color(0xFF1A5C3A)
private val GreenLight     = Color(0xFFE8F5EE)
private val GreenText      = Color(0xFF1A5C3A)
private val TextPrimary    = Color(0xFF1A1A1A)
private val TextSecondary  = Color(0xFF7A7A7A)
private val StarColor      = Color(0xFFF4B400)
private val ActiveDotColor = Color(0xFF2ECC71)
private val CardBg         = Color.White

data class HelperReview(
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)

data class ServiceWithPrice(
    val name: String,
    val price: String,
    val serviceId: Int = 0
)

data class HelperProfileData(
    val name: String,
    val email: String = "",
    val distance: String,
    val rating: Float,
    val isActive: Boolean = false,   // API doesn't return online status yet
    val about: String,
    val services: List<ServiceWithPrice>,
    val experience: String,
    val languages: String,
    val availabilityStart: String,
    val availabilityEnd: String,
    val dob: String = "",
    val address: String = "",
    val isPoliceVerified: Boolean = false,
    val reviews: List<HelperReview> = emptyList(),
    val reviewCount: Int,          // number of reviews, shown next to the rating
    val workingDays: List<String>,
    val area: String = "",         // shown in the one-line location
    val city: String = "",         // shown in the one-line location
    val photoUrl: String? = null   // full image URL, loaded with Coil
)

val MOCK_HELPER = HelperProfileData(
    name              = "Ramesh Kumar",
    email             = "ramesh.kumar@example.com",
    distance          = "0.8 km away",
    rating            = 4.9f,
    isActive          = true,
    about             = "Experienced and trustworthy home helper with 5 years of experience working with families across Lucknow. Speaks Hindi and English. Background verified and highly rated.",
    services          = listOf(
        ServiceWithPrice("Cooking",  "₹250/hr"),
        ServiceWithPrice("Cleaning", "₹200/hr"),
        ServiceWithPrice("Eldercare",  "₹150/hr")
    ),
    experience        = "5 years",
    languages         = "Hindi, English",
    availabilityStart = "9:00 AM",
    availabilityEnd   = "6:00 PM",
    dob               = "12 March 1990",
    address           = "House No. 45, Sector 12, Indira Nagar, Lucknow, UP",
    isPoliceVerified  = true,
    reviews           = listOf(
        HelperReview("Priya S.", 4, "Very punctual and thorough with cleaning. Would book again.", "2 days ago"),
        HelperReview("Arun M.",  4, "Cooked amazing food. My family loved it.", "1 week ago"),
    ),
    reviewCount       = 22,
    workingDays       = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
    area              = "Indira Nagar",
    city              = "Lucknow"
)

@Composable
fun HelperProfileScreen(
    helperId:  String  = "",
    helper:    HelperProfileData = MOCK_HELPER,
    onBookNow: (String) -> Unit = {},
    onBack:    () -> Unit = {},
    onChat:    () -> Unit = {},
    onViewReviews: (String) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Column(modifier = Modifier.fillMaxSize()) {

        // ── Header: back arrow + title ──
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
                text       = "Helper Profile",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Top card: photo (+ online dot), name, location, rating, About ──
                item {
                    Card(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Photo with the online dot on its bottom-right corner
                            Box(modifier = Modifier.size(96.dp)) {
                                Box(
                                    modifier         = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(GreenPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model              = helper.photoUrl,
                                        contentDescription = "Helper",
                                        contentScale       = ContentScale.Crop,
                                        placeholder        = painterResource(R.drawable.ic_profile_placeholder),
                                        error              = painterResource(R.drawable.ic_profile_placeholder),
                                        fallback           = painterResource(R.drawable.ic_profile_placeholder),
                                        modifier           = Modifier.fillMaxSize()
                                    )
                                }

                                if (helper.isActive) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(ActiveDotColor)
                                        )
                                    }
                                }
                            }

                            // Name + "Online" badge
                            Spacer(Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text       = helper.name,
                                    fontSize   = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary
                                )
                                if (helper.isActive) {
                                    Spacer(Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ActiveDotColor.copy(alpha = 0.12f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(ActiveDotColor)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text       = "Online",
                                                fontSize   = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color      = ActiveDotColor
                                            )
                                        }
                                    }
                                }
                            }

                            // Area, city and distance - all on one line
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector        = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint               = TextSecondary,
                                    modifier           = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text     = listOf(listOf(helper.area, helper.city).filter { it.isNotBlank() }.joinToString(", "), helper.distance).filter { it.isNotBlank() }.joinToString(" • "),
                                    fontSize = 13.sp,
                                    color    = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Rating + number of reviews (tap to open the reviews screen)
                            Spacer(Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(GreenLight)
                                    .clickable { onViewReviews(helperId) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Icon(
                                    painter            = painterResource(R.drawable.star),
                                    contentDescription = null,
                                    tint               = StarColor,
                                    modifier           = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text       = String.format(Locale.US, "%.1f", helper.rating),
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text       = "• ${helper.reviewCount} reviews",
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color      = GreenText
                                )
                            }

                            // About - merged into this same card
                            Spacer(Modifier.height(16.dp))
                            HorizontalDivider(color = Color(0xFFF0F0F0))
                            Spacer(Modifier.height(12.dp))
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text          = "ABOUT",
                                    fontSize      = 13.sp,
                                    fontWeight    = FontWeight.Bold,
                                    color         = GreenPrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text       = helper.about,
                                    fontSize   = 13.sp,
                                    color      = TextSecondary,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // ── Services with prices ─────────────────────────────────────
                item {
                    SectionCard(title = "SERVICES & PRICE") {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            helper.services.forEach { service ->
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
                        InfoBox(label = "EXPERIENCE", value = helper.experience, modifier = Modifier.weight(1f))
                        InfoBox(label = "LANGUAGES",  value = helper.languages,  modifier = Modifier.weight(1f))
                    }
                }

                // ── Availability (days + time) ───────────────────────────────
                item {
                    SectionCard(title = "AVAILABILITY") {
                        val allDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            allDays.forEach { day ->
                                val isWorking = helper.workingDays.contains(day)
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isWorking) GreenPrimary else GreenLight)
                                        .border(
                                            1.dp,
                                            if (isWorking) GreenPrimary else Color(0xFFE0E0E0),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text       = day.take(1),
                                        fontSize   = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color      = if (isWorking) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(GreenLight)
                                .border(1.dp, GreenPrimary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text       = "${helper.availabilityStart} – ${helper.availabilityEnd}",
                                fontSize   = 13.sp,
                                color      = GreenText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // ── Bottom actions ───────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                OutlinedButton(
                    onClick  = onChat,
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
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, color = TextSecondary, letterSpacing = 0.5.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}
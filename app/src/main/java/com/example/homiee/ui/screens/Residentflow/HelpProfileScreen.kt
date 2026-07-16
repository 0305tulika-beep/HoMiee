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
private val ActiveDotColor = Color(0xFF2ECC71)

data class HelperReview(
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)

data class ServiceWithPrice(val name: String, val price: String)

data class HelperProfileData(
    val name: String,
    val email: String,
    val distance: String,
    val rating: Float,
    val isActive: Boolean,
    val about: String,
    val services: List<ServiceWithPrice>,
    val experience: String,
    val languages: String,
    val availabilityStart: String,
    val availabilityEnd: String,
    val dob: String,
    val address: String,
    val isPoliceVerified: Boolean,
    val reviews: List<HelperReview>,
    val reviewCount: Int,
    val workingDays: List<String>
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
        ServiceWithPrice("Laundry",  "₹150/hr")
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
    workingDays       = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
)

@Composable
fun HelperProfileScreen(
    helperId:  String  = "",
    onBookNow: (String) -> Unit = {},
    onBack:    () -> Unit = {},
    onChat:    () -> Unit = {},
    onViewVerifiedDocuments: () -> Unit = {},
    onViewReviews: (String) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

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

                // ── Hero card: photo + name + about + personal details + verification ──
                item {
                    Card(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            // Photo + name + email + distance + rating
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(72.dp),
                                    contentAlignment = Alignment.Center
                                ) {
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

                                    if (MOCK_HELPER.isActive) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(13.dp)
                                                    .clip(CircleShape)
                                                    .background(ActiveDotColor)
                                            )
                                        }
                                    }
                                }
                                Spacer(Modifier.width(14.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text       = MOCK_HELPER.name,
                                            fontSize   = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color      = TextPrimary
                                        )
                                        if (MOCK_HELPER.isActive) {
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
                                                        "Online",
                                                        fontSize   = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color      = ActiveDotColor
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text     = MOCK_HELPER.email,
                                        fontSize = 12.sp,
                                        color    = TextSecondary
                                    )
                                    Spacer(Modifier.height(4.dp))
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

                            // ── Personal Details merged directly below About ──────────
                            Spacer(Modifier.height(14.dp))
                            HorizontalDivider(color = Color(0xFFF0F0F0))
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text          = "Personal Details",
                                fontSize      = 13.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = GreenPrimary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier              = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Date of Birth", fontSize = 13.sp, color = TextSecondary)
                                    Text(
                                        text       = MOCK_HELPER.dob,
                                        fontSize   = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color      = TextPrimary
                                    )
                                }
                                Column {
                                    Text("Address", fontSize = 13.sp, color = TextSecondary)
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text       = MOCK_HELPER.address,
                                        fontSize   = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color      = TextPrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }

                            // ── Verified Documents merged directly below Personal Details ──
                            Spacer(Modifier.height(14.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(GreenLight)
                                    .border(1.dp, GreenPrimary, RoundedCornerShape(12.dp))
                                    .clickable { onViewVerifiedDocuments() }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text       = "View Verified Documents",
                                        fontSize   = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color      = GreenText
                                    )
                                    if (MOCK_HELPER.isPoliceVerified) {
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            text       = "2 documents verified",
                                            fontSize   = 11.sp,
                                            color      = ActiveDotColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Text(
                                    text       = "View →",
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = GreenPrimary
                                )
                            }
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
                        val allDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            allDays.forEach { day ->
                                val isWorking = MOCK_HELPER.workingDays.contains(day)
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(GreenLight)
                                .border(1.dp, GreenPrimary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text       = "${MOCK_HELPER.availabilityStart} – ${MOCK_HELPER.availabilityEnd}",
                                fontSize   = 13.sp,
                                color      = GreenText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }

                // ── View Helper's Reviews (links to separate screen) ─────────
                item {
                    Card(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onViewReviews(helperId) },
                        shape     = RoundedCornerShape(14.dp),
                        colors    = CardDefaults.cardColors(containerColor = CardBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text       = "View Helper's Reviews",
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary
                                )
                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter            = painterResource(R.drawable.star),
                                        contentDescription = null,
                                        tint               = StarColor,
                                        modifier           = Modifier.size(13.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text     = "${String.format("%.1f", MOCK_HELPER.rating)} · ${MOCK_HELPER.reviewCount} reviews",
                                        fontSize = 12.sp,
                                        color    = TextSecondary
                                    )
                                }
                            }
                            Text(
                                text       = "View →",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color      = GreenPrimary
                            )
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
                    onClick = onChat,
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
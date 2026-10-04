package com.example.homiee.ui.screens.Residentflow

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.components.statusBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.homiee.viewmodel.NearbyHelpersViewModel

// ── Shared colors ─────────────────────────────────────────────────────────
private val ActiveDotColor   = Color(0xFF2ECC71)
private val CardMint         = Color(0xFFEAF6F2)   // soft mint-green card background
private val CardMintEdge     = Color(0xFFD7EEE6)   // subtle border
private val TealAccent       = Color(0xFF1CA88C)   // "May" / location icon color
private val StatusGreenBg    = Color(0xFFCDEFDD)
private val StatusGreenFg    = Color(0xFF1E9E5A)
private val SosRed           = Color(0xFFE6483A)
private val SosCardBg        = Color(0xFFFDEDEC)
private val SosCardEdge      = Color(0xFFF8D3CF)
private val SosBodyText      = Color(0xFF7A6E6C)
private val AvatarGreen      = Color(0xFF2E7D67)
private val CategoryCircleBg = Color(0xFFF0F5F2)
private val ActivityTitle    = Color(0xFF16211D)
private val ActivitySubtitle = Color(0xFF6B7570)
private val ChevronGray      = Color(0xFF9AA6A1)

@Composable
fun ResidentHomeScreen(
    recentActivities: List<BookingItem> = emptyList(),
    onNavItemClick:   (String) -> Unit = {},
    onBookClick:      (String) -> Unit = {},
    onCategoryClick:  (String) -> Unit = {},
    onActivityClick:  (String) -> Unit = {}
) {
    val context = LocalContext.current
    val nearbyViewModel: NearbyHelpersViewModel = viewModel()
    val nearbyState by nearbyViewModel.uiState.collectAsState()

    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.HOME,
                onTabSelected = { tab ->
                    val route = when (tab) {
                        NavTab.HOME     -> Routes.HOME_RES
                        NavTab.SEARCH   -> Routes.SEARCH
                        NavTab.BOOKINGS -> Routes.BOOKINGS
                        NavTab.MESSAGE  -> Routes.MESSAGES
                        NavTab.ACCOUNT  -> Routes.ACCOUNT
                    }
                    onNavItemClick(route)
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter            = painterResource(id = R.drawable.bg1),
                contentDescription = null,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Green header: logo ─────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(statusBarsPadding())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Image(
                        painter            = painterResource(id = R.drawable.logotext),
                        contentDescription = "HoMiee",
                        modifier           = Modifier.height(50.dp)
                    )
                }

                // ── Scrollable content ─────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {

                    // ── Quick Categories — wrapped in a white card ──
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(20.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text       = "QUICK CATEGORIES",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                CategoryItem(
                                    iconRes = R.drawable.img_3,
                                    label   = "Cleaning",
                                    onClick = { onCategoryClick("Cleaning") }
                                )
                                CategoryItem(
                                    iconRes = R.drawable.img_4,
                                    label   = "Cooking",
                                    onClick = { onCategoryClick("Cooking") }
                                )
                                CategoryItem(
                                    iconRes = R.drawable.img_5,
                                    label   = "Laundry",
                                    onClick = { onCategoryClick("Laundry") }
                                )
                                CategoryItem(
                                    iconRes = R.drawable.img_6,
                                    label   = "Babysitting",
                                    onClick = { onCategoryClick("Babysitting") }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Nearby verified helpers ──
                    Text(
                        text       = "Nearby verified helpers",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.height(10.dp))

                    when {
                        nearbyState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(120.dp),
                                contentAlignment = Alignment.Center
                            ) { CircularProgressIndicator(color = GreenDark) }
                        }
                        nearbyState.errorMessage != null -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = nearbyState.errorMessage ?: "",
                                    fontSize = 13.sp,
                                    color = TextMuted,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { nearbyViewModel.load() }) {
                                    Text("Retry", color = GreenDark, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        nearbyState.helpers.isEmpty() -> {
                            Text("No helpers found nearby yet", fontSize = 13.sp, color = TextMuted)
                        }
                        else -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                nearbyState.helpers.forEach { helper ->
                                    HelperCard(
                                        name     = helper.name,
                                        service  = helper.services,
                                        rating   = helper.rating,
                                        photoUrl = helper.photoUrl,
                                        isActive = false,
                                        onClick  = { onBookClick(helper.id) }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Recent Activity — sourced from real bookings ──
                    Text(
                        text       = "RECENT ACTIVITY",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.height(10.dp))

                    val recentToShow = recentActivities
                        .filter { it.status == BookingTab.ACTIVE || it.status == BookingTab.UPCOMING }
                        .take(2)   // show at most 2 on Home; full list lives on Bookings screen

                    if (recentToShow.isEmpty()) {
                        Text(
                            text     = "No recent activity yet",
                            fontSize = 13.sp,
                            color    = TextMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            recentToShow.forEach { booking ->
                                ActivityCard(
                                    booking = booking,
                                    onClick = { onActivityClick(booking.id) }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Quick Actions ──
                    Text(
                        text       = "QUICK ACTIONS",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Spacer(Modifier.height(10.dp))

                    SosButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:112")
                            }
                            context.startActivity(intent)
                        }
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

// ---------- Activity Card ----------
@Composable
private fun ActivityCard(
    booking: BookingItem,
    onClick: () -> Unit = {}
) {
    val statusText = when (booking.status) {
        BookingTab.ACTIVE    -> "In Progress"
        BookingTab.UPCOMING  -> "Confirmed"
        BookingTab.COMPLETED -> "Completed" // shouldn't show here, but safe fallback
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CardMint)
            .border(1.dp, CardMintEdge, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 18.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Helper avatar (initials) instead of a date block —
        // your BookingItem doesn't carry a clean day/month split
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(GreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = booking.initials,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(14.dp))

        // Middle info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = booking.service,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ActivityTitle
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = booking.helperName,
                fontSize = 13.sp,
                color = ActivitySubtitle
            )
            if (booking.status == BookingTab.UPCOMING && booking.bookingDate.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${booking.bookingDate} · ${booking.bookingTime}",
                    fontSize = 12.sp,
                    color = ActivitySubtitle
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Status pill + chevron
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(StatusGreenBg)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = statusText,
                    color = StatusGreenFg,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ChevronGray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ---------- Emergency SOS Card ----------
@Composable
private fun SosButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SosCardBg)
            .border(1.dp, SosCardEdge, RoundedCornerShape(18.dp))
            .padding(vertical = 18.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular SOS badge
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SosRed),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SOS",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Emergency SOS",
                color = SosRed,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tap to alert your emergency contacts in case of any urgent situation.",
                color = SosBodyText,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        // Outlined "Tap to SOS" button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, SosRed, RoundedCornerShape(24.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Tap to SOS",
                color = SosRed,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CategoryItem(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(CategoryCircleBg, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter            = painterResource(id = iconRes),
                contentDescription = label,
                modifier           = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text       = label,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Medium,
            color      = TextPrimary
        )
    }
}

@Composable
private fun HelperCard(
    name: String,
    service: String,
    rating: String,
    photoUrl: String? = null,
    isActive: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        modifier  = Modifier.width(170.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier         = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier         = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(AvatarGreen),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUrl != null) {
                        AsyncImage(
                            model              = photoUrl,
                            contentDescription = "Profile",
                            contentScale       = ContentScale.Crop,
                            placeholder        = painterResource(id = R.drawable.ic_profile_placeholder),
                            error              = painterResource(id = R.drawable.ic_profile_placeholder),
                            modifier           = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter            = painterResource(id = R.drawable.ic_profile_placeholder),
                            contentDescription = "Profile",
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )
                    }
                }

                if (isActive) {
                    Box(
                        modifier         = Modifier
                            .align(Alignment.BottomEnd)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(ActiveDotColor)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text       = name,
                fontWeight = FontWeight.Bold,
                fontSize   = 15.sp,
                color      = TextPrimary,
                maxLines   = 1
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text     = service,
                fontSize = 13.sp,
                color    = TextMuted,
                maxLines = 1
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter            = painterResource(id = R.drawable.star),
                    contentDescription = "rating",
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(rating, fontSize = 13.sp, color = TextMuted)
            }

            Spacer(Modifier.height(16.dp))

            // Custom-built Book button — no PNG asset
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = GreenDark)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "Book",
                    color      = Color.White,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
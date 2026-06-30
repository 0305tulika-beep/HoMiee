package com.example.homiee.ui.screens.Residentflow

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White

// ── Active status colors ─────────────────────────────────────────────────────
private val ActiveDotColor = Color(0xFF2ECC71)

@Composable
fun ResidentHomeScreen(
    recentActivities: List<BookingItem> = emptyList(),   // ← NEW: real bookings, fed from NavGraph
    onNavItemClick:   (String) -> Unit = {},
    onBookClick:      (String) -> Unit = {},
    onCategoryClick:  (String) -> Unit = {},
    onActivityClick:  (String) -> Unit = {}               // ← NEW: tap an activity card → its details
) {
    val context = LocalContext.current
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    Scaffold(
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
                painter            = painterResource(id = R.drawable.bg),
                contentDescription = null,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Green header: greeting + profile icon ──────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(systemBarsPadding())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Hi, Priya",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color      = White
                    )
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D67)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter            = painterResource(id = R.drawable.ic_profile_placeholder),
                            contentDescription = "Profile",
                            modifier           = Modifier.size(24.dp)
                        )
                    }
                }

                // ── Scrollable content ─────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {

                    // ── Quick Categories ──
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter            = painterResource(id = R.drawable.box1),
                            contentDescription = null,
                            contentScale       = ContentScale.FillBounds,
                            modifier           = Modifier.fillMaxWidth()
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text       = "QUICK CATEGORIES",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color      = TextPrimary
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Image(
                                    painter            = painterResource(id = R.drawable.img_3),
                                    contentDescription = "Cleaning",
                                    modifier           = Modifier
                                        .width(56.dp)
                                        .clickable { onCategoryClick("Cleaning") }
                                )
                                Image(
                                    painter            = painterResource(id = R.drawable.img_4),
                                    contentDescription = "Cooking",
                                    modifier           = Modifier
                                        .width(56.dp)
                                        .clickable { onCategoryClick("Cooking") }
                                )
                                Image(
                                    painter            = painterResource(id = R.drawable.img_5),
                                    contentDescription = "Laundry",
                                    modifier           = Modifier
                                        .width(56.dp)
                                        .clickable { onCategoryClick("Laundry") }
                                )
                                Image(
                                    painter            = painterResource(id = R.drawable.img_6),
                                    contentDescription = "Babysitting",
                                    modifier           = Modifier
                                        .width(56.dp)
                                        .clickable { onCategoryClick("Babysitting") }
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HelperCard(name = "Ramesh Kumar", service = "Cleaning", rating = "4.9", isActive = true)
                        HelperCard(name = "Sunita Devi",  service = "Cooking",  rating = "4.8", isActive = false)
                        HelperCard(name = "Priya Singh",  service = "Laundry",  rating = "4.7", isActive = true)
                        HelperCard(name = "Anita Rao",    service = "Laundry",  rating = "4.8", isActive = false)
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Recent Activity — now sourced from real bookings ──
                    Text(
                        text       = "RECENT ACTIVITY",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenDark
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
                                    title     = "${booking.service}: ${booking.helperName}",
                                    subtitle  = if (booking.status == BookingTab.ACTIVE)
                                        "In Progress"
                                    else
                                        "${booking.bookingDate}, ${booking.bookingTime}",
                                    statusRes = if (booking.status == BookingTab.ACTIVE)
                                        R.drawable.btn_ac
                                    else
                                        R.drawable.btn_up,
                                    onClick   = { onActivityClick(booking.id) }
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
                        color      = GreenDark
                    )
                    Spacer(Modifier.height(10.dp))

                    Image(
                        painter            = painterResource(id = R.drawable.btn_1),
                        contentDescription = "Emergency SOS",
                        contentScale       = ContentScale.FillBounds,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clickable {
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

@Composable
private fun ActivityCard(
    title: String,
    subtitle: String,
    statusRes: Int,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter            = painterResource(id = R.drawable.box3),
            contentDescription = null,
            contentScale       = ContentScale.FillBounds,
            modifier           = Modifier.fillMaxSize()
        )
        Row(
            modifier              = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(title,    fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(subtitle, fontSize   = 12.sp, color = Color.Black.copy(alpha = 0.7f))
            }
            Image(
                painter            = painterResource(id = statusRes),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun HelperCard(
    name: String,
    service: String,
    rating: String,
    isActive: Boolean = true
) {
    androidx.compose.material3.Card(
        modifier  = Modifier.width(170.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier         = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D67)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter            = painterResource(id = R.drawable.ic_profile_placeholder),
                        contentDescription = "Profile",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                }

                if (isActive) {
                    Box(
                        modifier = Modifier
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

            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
            }

            Spacer(Modifier.height(4.dp))
            Text(service, fontSize = 13.sp, color = TextMuted)
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

            Image(
                painter            = painterResource(id = R.drawable.btn_book),
                contentDescription = "Book",
                contentScale       = ContentScale.Fit,
                modifier           = Modifier
                    .width(100.dp)
                    .height(30.dp)
            )
        }
    }
}
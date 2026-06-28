package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark

// ── Design tokens ──────────────────────────────────────────────────────────────
private val GreenPrimary  = Color(0xFF1A5C3A)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val StarColor     = Color(0xFFF4B400)
private val CardBg        = Color.White

// ── Data ───────────────────────────────────────────────────────────────────────
enum class BookingTab { UPCOMING, ACTIVE, COMPLETED }

data class BookingItem(
    val id: String,
    val helperName: String,
    val service: String,
    val rating: Float,
    val status: BookingTab,
    val isPending: Boolean = false,
    val bookingDate: String = "",
    val bookingTime: String = "",
    val initials: String = helperName.take(2).uppercase()
)

private val MOCK_BOOKINGS = listOf(
    BookingItem("b001", "Ramesh Kumar", "Cleaning", 4.9f, BookingTab.ACTIVE),
    BookingItem("b002", "Ramesh Kumar", "Cleaning", 4.9f, BookingTab.ACTIVE),
    BookingItem("b003", "Sunita Devi",  "Cooking",  4.8f, BookingTab.UPCOMING,
        isPending = true,  bookingDate = "Jun 15, 2026", bookingTime = "10:00 AM"),
    BookingItem("b004", "Kavita Singh", "Laundry",  4.6f, BookingTab.UPCOMING,
        isPending = false, bookingDate = "Jun 16, 2026", bookingTime = "2:00 PM"),
    BookingItem("b005", "Meena Verma",  "Laundry",  4.7f, BookingTab.COMPLETED),
)

// ── Screen ─────────────────────────────────────────────────────────────────────
@Composable
fun BookingsScreen(
    bookings: List<BookingItem>,
    onNavItemClick: (String) -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    initialTab: BookingTab = BookingTab.UPCOMING
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var selectedTab by remember { mutableStateOf(initialTab) }

    val filteredBookings = remember(selectedTab) {
        MOCK_BOOKINGS.filter { it.status == selectedTab }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.BOOKINGS,
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
                painter            = painterResource(R.drawable.bg),
                contentDescription = null,
                contentScale       = ContentScale.FillBounds,
                modifier           = Modifier.fillMaxSize()
            )

            LazyColumn(
                modifier       = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                // ── Header ───────────────────────────────────────────────────
                item {
                    Text(
                        text       = "MY BOOKINGS",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White,
                        modifier   = Modifier
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    )
                }

                // ── Tab row ──────────────────────────────────────────────────
                item {
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BookingTab.values().forEach { tab ->
                            BookingTabChip(
                                label      = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                                isSelected = selectedTab == tab,
                                onClick    = { selectedTab = tab },
                                modifier   = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // ── Cards ────────────────────────────────────────────────────
                items(filteredBookings, key = { it.id }) { booking ->
                    if (booking.isPending) {
                        PendingBookingCard(
                            booking  = booking,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                        )
                    } else {
                        BookingCard(
                            booking        = booking,
                            onDetailsClick = onDetailsClick,
                            modifier       = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }


                // ── Empty state ──────────────────────────────────────────────
                if (filteredBookings.isEmpty()) {
                    item {
                        Box(
                            modifier         = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No ${selectedTab.name.lowercase()} bookings",
                                color    = TextSecondary,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Tab chip ───────────────────────────────────────────────────────────────────
@Composable
private fun BookingTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier         = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) GreenPrimary else Color.White)
            .border(1.dp, if (isSelected) GreenPrimary else Color(0xFFCCCCCC), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            color      = if (isSelected) Color.White else TextSecondary,
            fontSize   = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ── Pending booking card (narrow, amber) ───────────────────────────────────────
@Composable
private fun PendingBookingCard(
    booking: BookingItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.weight(1f)
            ) {
                Box(
                    modifier         = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF57F17)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = booking.initials,
                        color      = Color.White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text       = booking.helperName,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary
                    )
                    Text(
                        text     = "${booking.service} · ${booking.bookingDate} · ${booking.bookingTime}",
                        fontSize = 11.sp,
                        color    = TextSecondary
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF57F17))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = "Pending",
                    color      = Color.White,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Full booking card ──────────────────────────────────────────────────────────
@Composable
private fun BookingCard(
    booking: BookingItem,
    onDetailsClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Helper info row ──────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = booking.initials,
                        color      = Color.White,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = booking.helperName,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary
                    )
                    Text(
                        text     = booking.service,
                        fontSize = 13.sp,
                        color    = TextSecondary
                    )
                    Spacer(Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter            = painterResource(R.drawable.star),
                            contentDescription = null,
                            tint               = StarColor,
                            modifier           = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text     = String.format("%.1f", booking.rating),
                            fontSize = 12.sp,
                            color    = TextSecondary
                        )
                    }
                }

                // Status badge
                val badgeColor = when (booking.status) {
                    BookingTab.ACTIVE    -> Color(0xFFE8F5EE)
                    BookingTab.UPCOMING  -> Color(0xFFFFF8E1)
                    BookingTab.COMPLETED -> Color(0xFFF5F5F5)
                }
                val badgeTextColor = when (booking.status) {
                    BookingTab.ACTIVE    -> Color(0xFF1A5C3A)
                    BookingTab.UPCOMING  -> Color(0xFFF57F17)
                    BookingTab.COMPLETED -> Color(0xFF757575)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = booking.status.name.lowercase().replaceFirstChar { it.uppercase() },
                        color      = badgeTextColor,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
            Spacer(Modifier.height(14.dp))

            // ── 3-button row ─────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick        = { },
                    shape          = RoundedCornerShape(10.dp),
                    border         = BorderStroke(1.5.dp, GreenPrimary),
                    colors         = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                    modifier       = Modifier.weight(1f).height(44.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text("Chat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = GreenDark)
                }

                OutlinedButton(
                    onClick        = { onDetailsClick(booking.id)},
                    shape          = RoundedCornerShape(10.dp),
                    border         = BorderStroke(1.5.dp, GreenPrimary),
                    colors         = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                    modifier       = Modifier.weight(1f).height(44.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Text("Details", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = GreenDark)
                }

                when (booking.status) {
                    BookingTab.ACTIVE -> Button(
                        onClick        = { },
                        shape          = RoundedCornerShape(10.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        modifier       = Modifier.weight(1f).height(44.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("SOS", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    BookingTab.UPCOMING -> Button(
                        onClick        = { },
                        shape          = RoundedCornerShape(10.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A7A7A)),
                        modifier       = Modifier.weight(1f).height(44.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("Cancel", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    BookingTab.COMPLETED -> Button(
                        onClick        = { },
                        shape          = RoundedCornerShape(10.dp),
                        colors         = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier       = Modifier.weight(1f).height(44.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Text("Review", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // ── Live activity banner — Active only ───────────────────────────
            if (booking.status == BookingTab.ACTIVE) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE8F5EE))
                        .clickable { }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier              = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GreenPrimary)
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    text       = "Helper is currently active",
                                    fontSize   = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = Color(0xFF1A5C3A)
                                )
                                Text(
                                    text     = "Tap to track activity",
                                    fontSize = 11.sp,
                                    color    = Color(0xFF2E7D55)
                                )
                            }
                        }
                        Icon(
                            imageVector        = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint               = GreenPrimary,
                            modifier           = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
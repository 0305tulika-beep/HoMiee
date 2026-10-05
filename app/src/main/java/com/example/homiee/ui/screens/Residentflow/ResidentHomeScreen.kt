package com.example.homiee.ui.screens.Residentflow

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.components.statusBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.viewmodel.NearbyHelpersViewModel


// ── Colors ────────────────────────────────────────────────────────────────

private val CategoryCircleBg   = Color(0xFFE4F3EC)
private val CategoryCircleEdge = Color(0xFFCFE8DD)
private val CardMint           = Color(0xFFEAF6F2)
private val CardMintEdge       = Color(0xFFD7EEE6)
private val StatusGreenBg      = Color(0xFFCDEFDD)
private val StatusGreenFg      = Color(0xFF1E9E5A)
private val SosRed             = Color(0xFFE6483A)
private val SosCardBg          = Color(0xFFFDEDEC)
private val SosCardEdge        = Color(0xFFF8D3CF)
private val SosBodyText        = Color(0xFF7A6E6C)
private val AvatarGreen        = Color(0xFF2E7D67)
private val VerifiedGreen      = Color(0xFF1E9E5A)
private val ActivityTitle      = Color(0xFF16211D)
private val ActivitySubtitle   = Color(0xFF6B7570)
private val ChevronGray        = Color(0xFF9AA6A1)


// ── Quick categories data ─────────────────────────────────────────────────

private data class QuickCategory(
    @DrawableRes val icon: Int,
    val label: String
)

private val quickCategories = listOf(
    QuickCategory(R.drawable.ic_cleaning,    "Cleaning"),
    QuickCategory(R.drawable.ic_cooking,     "Cooking"),
    QuickCategory(R.drawable.ic_eldercare,   "Eldercare"),
    QuickCategory(R.drawable.ic_babysitting, "Babysitting")
)


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

    TransparentStatusBarWhiteNavBar(
        lightStatusBarIcons = true
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        bottomBar = {
            BottomNavBar(
                selectedTab = NavTab.HOME,

                onTabSelected = { tab ->

                    val route = when (tab) {

                        NavTab.HOME ->
                            Routes.HOME_RES

                        NavTab.SEARCH ->
                            Routes.SEARCH

                        NavTab.BOOKINGS ->
                            Routes.BOOKINGS

                        NavTab.MESSAGE ->
                            Routes.MESSAGES

                        NavTab.ACCOUNT ->
                            Routes.ACCOUNT
                    }

                    onNavItemClick(route)
                }
            )
        },

        containerColor = Color.Transparent

    ) { innerPadding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // ── Background ──

            Image(
                painter = painterResource(R.drawable.bg1),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Header: logo + tagline ──

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(statusBarsPadding())
                        .padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        ),
                    horizontalAlignment = Alignment.Start
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.logotext
                        ),
                        contentDescription = "HoMiee",
                        modifier = Modifier.height(50.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Care. Comfort. Community.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        letterSpacing = 0.6.sp,
                        color = Color.White.copy(alpha = 0.88f),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }


                // ── Scrollable content ──

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(horizontal = 16.dp)
                ) {

                    // ── Quick categories ──

                    QuickCategoriesCard(
                        onCategoryClick = onCategoryClick
                    )

                    Spacer(
                        Modifier.height(24.dp)
                    )


                    // ── Nearby verified helpers ──

                    SectionHeader(
                        icon = Icons.Filled.LocationOn,
                        title = "Nearby verified helpers",
                        onActionClick = {
                            onNavItemClick(Routes.SEARCH)
                        }
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )


                    when {

                        nearbyState.isLoading -> {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),

                                contentAlignment = Alignment.Center

                            ) {

                                CircularProgressIndicator(
                                    color = GreenDark
                                )
                            }
                        }


                        nearbyState.errorMessage != null -> {

                            Row(
                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween,

                                verticalAlignment =
                                    Alignment.CenterVertically

                            ) {

                                Text(
                                    text =
                                        nearbyState.errorMessage ?: "",

                                    fontSize = 13.sp,

                                    color = TextMuted,

                                    modifier =
                                        Modifier.weight(1f)
                                )


                                TextButton(
                                    onClick = {
                                        nearbyViewModel.load()
                                    }
                                ) {

                                    Text(
                                        "Retry",
                                        color = GreenDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }


                        nearbyState.helpers.isEmpty() -> {

                            Text(
                                "No helpers found nearby yet",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }


                        else -> {

                            // Two cards fill the width;
                            // more helpers scroll sideways

                            BoxWithConstraints(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                val cardWidth =
                                    (maxWidth - 12.dp) / 2


                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(
                                            rememberScrollState()
                                        ),

                                    horizontalArrangement =
                                        Arrangement.spacedBy(12.dp)

                                ) {

                                    nearbyState.helpers.forEach { helper ->

                                        HelperCard(
                                            name = helper.name,
                                            service = helper.services,
                                            rating = helper.rating,
                                            photoUrl = helper.photoUrl,
                                            isVerified = true,

                                            onClick = {
                                                onBookClick(helper.id)
                                            },

                                            modifier = Modifier
                                                .width(cardWidth)
                                        )
                                    }
                                }
                            }
                        }
                    }


                    Spacer(
                        Modifier.height(24.dp)
                    )


                    // ── Recent activity ──

                    SectionHeader(
                        icon = Icons.Outlined.Schedule,
                        title = "RECENT ACTIVITY"
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )


                    val recentToShow =
                        recentActivities
                            .filter {
                                it.status == BookingTab.ACTIVE ||
                                        it.status == BookingTab.UPCOMING
                            }
                            .take(2)


                    if (recentToShow.isEmpty()) {

                        Text(
                            "No recent activity yet",
                            fontSize = 13.sp,
                            color = TextMuted
                        )

                    } else {

                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            recentToShow.forEach { booking ->

                                ActivityCard(
                                    booking = booking,

                                    onClick = {
                                        onActivityClick(booking.id)
                                    }
                                )
                            }
                        }
                    }


                    Spacer(
                        Modifier.height(24.dp)
                    )


                    // ── Quick actions ──

                    SectionHeader(
                        icon = Icons.Filled.FlashOn,
                        title = "QUICK ACTIONS"
                    )

                    Spacer(
                        Modifier.height(12.dp)
                    )


                    SosButton(
                        onClick = {

                            val intent =
                                Intent(Intent.ACTION_DIAL).apply {

                                    data =
                                        Uri.parse("tel:112")
                                }

                            context.startActivity(intent)
                        }
                    )


                    Spacer(
                        Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}


// ---------- Section header: icon + title (+ optional arrow) ----------

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
    onActionClick: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GreenDark,
            modifier = Modifier.size(24.dp)
        )

        Spacer(
            Modifier.width(10.dp)
        )

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )


        if (onActionClick != null) {

            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "See all",
                tint = GreenDark,

                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = onActionClick
                    )
            )
        }
    }
}


// ---------- Quick categories ----------

@Composable
private fun QuickCategoriesCard(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 14.dp
                )
        ) {

            quickCategories.forEach { category ->

                CategoryItem(
                    iconRes = category.icon,
                    label = category.label,

                    onClick = {
                        onCategoryClick(category.label)
                    },

                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun CategoryItem(
    @DrawableRes iconRes: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {

        Box(
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
                .background(CategoryCircleBg)
                .border(
                    1.dp,
                    CategoryCircleEdge,
                    CircleShape
                ),

            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(
                    id = iconRes
                ),

                contentDescription = label,

                tint = GreenDark,

                modifier = Modifier.size(28.dp)
            )
        }


        Spacer(
            Modifier.height(6.dp)
        )


        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            maxLines = 1
        )
    }
}


// ---------- Helper card ----------

@Composable
private fun HelperCard(
    name: String,
    service: String,
    rating: String,
    photoUrl: String? = null,
    isVerified: Boolean = true,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(104.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(AvatarGreen),

                    contentAlignment =
                        Alignment.Center
                ) {

                    if (photoUrl != null) {

                        AsyncImage(
                            model = photoUrl,

                            contentDescription =
                                "Profile",

                            contentScale =
                                ContentScale.Crop,

                            placeholder =
                                painterResource(
                                    id =
                                        R.drawable
                                            .ic_profile_placeholder
                                ),

                            error =
                                painterResource(
                                    id =
                                        R.drawable
                                            .ic_profile_placeholder
                                ),

                            modifier =
                                Modifier.fillMaxSize()
                        )

                    } else {

                        Image(
                            painter =
                                painterResource(
                                    id =
                                        R.drawable
                                            .ic_profile_placeholder
                                ),

                            contentDescription =
                                "Profile",

                            contentScale =
                                ContentScale.Crop,

                            modifier =
                                Modifier.fillMaxSize()
                        )
                    }
                }


                // Verified badge

                if (isVerified) {

                    Image(
                        painter =
                            painterResource(
                                id =
                                    R.drawable
                                        .ic_verifiedshield
                            ),

                        contentDescription =
                            "Verified",

                        modifier = Modifier
                            .align(
                                Alignment.BottomEnd
                            )
                            .size(32.dp)
                    )
                }
            }


            Spacer(
                Modifier.height(12.dp)
            )


            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary,
                maxLines = 1
            )


            Spacer(
                Modifier.height(2.dp)
            )


            Text(
                text = service,
                fontSize = 13.sp,
                color = TextMuted,
                maxLines = 1
            )


            Spacer(
                Modifier.height(8.dp)
            )


            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter =
                        painterResource(
                            id = R.drawable.star
                        ),

                    contentDescription =
                        "rating",

                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    Modifier.width(4.dp)
                )

                Text(
                    rating,
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }


            Spacer(
                Modifier.height(14.dp)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .background(GreenDark)
                    .clickable(
                        interactionSource =
                            remember {
                                MutableInteractionSource()
                            },

                        indication = null
                    ) {
                        onClick()
                    },

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "Book",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


// ---------- Activity card ----------

@Composable
private fun ActivityCard(
    booking: BookingItem,
    onClick: () -> Unit = {}
) {

    val statusText = when (booking.status) {

        BookingTab.ACTIVE ->
            "In Progress"

        BookingTab.UPCOMING ->
            "Confirmed"

        BookingTab.COMPLETED ->
            "Completed"
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(CardMint)
            .border(
                1.dp,
                CardMintEdge,
                RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(GreenDark),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = booking.initials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            Modifier.width(14.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = booking.service,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ActivityTitle
            )


            Spacer(
                Modifier.height(2.dp)
            )


            Text(
                text = booking.helperName,
                fontSize = 13.sp,
                color = ActivitySubtitle
            )


            if (
                booking.status == BookingTab.UPCOMING &&
                booking.bookingDate.isNotBlank()
            ) {

                Spacer(
                    Modifier.height(4.dp)
                )


                Text(
                    text =
                        "${booking.bookingDate} · ${booking.bookingTime}",

                    fontSize = 12.sp,

                    color = ActivitySubtitle
                )
            }
        }


        Spacer(
            Modifier.width(8.dp)
        )


        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(StatusGreenBg)
                    .padding(
                        horizontal = 14.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = statusText,
                    color = StatusGreenFg,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(
                Modifier.width(4.dp)
            )


            Icon(
                imageVector =
                    Icons.Filled.ChevronRight,

                contentDescription = null,

                tint = ChevronGray,

                modifier =
                    Modifier.size(20.dp)
            )
        }
    }
}


// ---------- Emergency SOS card ----------

@Composable
private fun SosButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(SosCardBg)
            .border(
                1.dp,
                SosCardEdge,
                RoundedCornerShape(18.dp)
            )
            .padding(
                vertical = 18.dp,
                horizontal = 18.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SosRed),

            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = "SOS",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            Modifier.width(16.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Emergency SOS",
                color = SosRed,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )


            Spacer(
                Modifier.height(4.dp)
            )


            Text(
                text =
                    "Tap to alert your emergency contacts in case of any urgent situation.",

                color = SosBodyText,

                fontSize = 12.sp,

                lineHeight = 16.sp
            )
        }


        Spacer(
            Modifier.width(12.dp)
        )


        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(24.dp)
                )
                .border(
                    1.5.dp,
                    SosRed,
                    RoundedCornerShape(24.dp)
                )
                .clickable(

                    interactionSource =
                        remember {
                            MutableInteractionSource()
                        },

                    indication = null

                ) {
                    onClick()
                }

                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
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
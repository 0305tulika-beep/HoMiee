package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import com.example.homiee.R
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.White

// ── Design tokens ──────────────────────────────────────────────────────────────
private val GreenPrimary     = Color(0xFF1A5C3A)
private val ChipSelectedBg   = Color(0xFF1A5C3A)
private val ChipSelectedText = Color.White
private val ChipUnselBg      = Color.White
private val ChipUnselBorder  = Color(0xFFCCCCCC)
private val ChipUnselText    = Color(0xFF333333)
private val TextPrimary      = Color(0xFF1A1A1A)
private val TextSecondary    = Color(0xFF7A7A7A)
private val StarColor        = Color(0xFFF4B400)
private val CardBg           = Color.White
private val ActiveDotColor   = Color(0xFF2ECC71)

// ── Data ───────────────────────────────────────────────────────────────────────
data class HelperCard(
    val id: String,
    val name: String,
    val service: String,
    val rating: Float,
    val photoUrl: String? = null,
    val isActive: Boolean = true,
    val initials: String = name.take(2).uppercase()
)

enum class SortOption(val label: String) {
    NEAREST("Nearest"),
    HIGHEST_RATED("Highest Rated"),
    LOWEST_COST("Lowest Cost")
}

private val SERVICE_FILTERS = listOf(
    "All", "Cleaning", "Cooking", "Laundry", "Babysitting", "Other"
)

private val TEST_HELPERS = listOf(
    HelperCard("001", "Priya Sharma", "Cleaning",    4.9f, isActive = true),
    HelperCard("002", "Sunita Devi",  "Cooking",     4.8f, isActive = false),
    HelperCard("003", "Meena Verma",  "Laundry",     4.8f, isActive = true),
    HelperCard("004", "Ramesh Kumar", "Babysitting", 4.7f, isActive = true),
    HelperCard("005", "Kavita Singh", "Cooking",     4.6f, isActive = false),
    HelperCard("006", "Anita Patel",  "Cleaning",    4.5f, isActive = true),
)

// ── Screen ─────────────────────────────────────────────────────────────────────
@Composable
fun SearchScreen(
    initialFilter: String = "All",                  // ← NEW: lets Home open Search pre-filtered
    onViewProfile: (String) -> Unit = {},
    onBook: (String) -> Unit = {},
    onNavItemClick: (String) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var searchQuery    by remember { mutableStateOf("") }
    var selectedSort   by remember { mutableStateOf(SortOption.NEAREST) }
    var selectedFilter by remember { mutableStateOf(initialFilter) }   // ← starts pre-filtered

    val displayedHelpers = remember(searchQuery, selectedSort, selectedFilter) {
        TEST_HELPERS
            .filter { h ->
                val matchFilter = selectedFilter == "All" ||
                        h.service.equals(selectedFilter, ignoreCase = true)
                val matchQuery = searchQuery.isBlank() ||
                        h.name.contains(searchQuery, ignoreCase = true) ||
                        h.service.contains(searchQuery, ignoreCase = true)
                matchFilter && matchQuery
            }
            .let { list ->
                when (selectedSort) {
                    SortOption.NEAREST       -> list
                    SortOption.HIGHEST_RATED -> list.sortedByDescending { it.rating }
                    SortOption.LOWEST_COST   -> list
                }
            }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.SEARCH,
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

                // ── Green header + search bar ─────────────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp)
                            .padding(top = 24.dp, bottom = 28.dp)
                    ) {
                        Column {
                            Text(
                                text       = "Find A Helper",
                                color      = Color.White,
                                fontSize   = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(
                                value         = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder   = {
                                    Text(
                                        "Search for helpers...",
                                        color    = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        painter            = painterResource(R.drawable.ic_search),
                                        contentDescription = "Search",
                                        tint               = TextSecondary,
                                        modifier           = Modifier.size(20.dp)
                                    )
                                },
                                singleLine = true,
                                shape      = RoundedCornerShape(14.dp),
                                colors     = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor   = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor      = Color.Transparent,
                                    unfocusedBorderColor    = Color.Transparent,
                                    cursorColor             = GreenPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // ── Service filter chips ─────────────────────────────────────
                item {
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(SERVICE_FILTERS) { filter ->
                            FilterChipItem(
                                label      = filter,
                                isSelected = selectedFilter == filter,
                                onClick    = { selectedFilter = filter }
                            )
                        }
                    }
                }

                // ── Sort by ──────────────────────────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text          = "SORT BY",
                            fontSize      = 13.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = White,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SortOption.values().forEach { option ->
                                FilterChipItem(
                                    label      = option.label,
                                    isSelected = selectedSort == option,
                                    onClick    = { selectedSort = option }
                                )
                            }
                        }
                    }
                }

                // ── Result count ─────────────────────────────────────────────
                item {
                    Text(
                        text     = "${displayedHelpers.size} HELPERS FOUND",
                        fontSize = 16.sp,
                        fontWeight    = FontWeight.Bold,
                        color    = GreenDark,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }

                // ── Helper cards ─────────────────────────────────────────────
                items(displayedHelpers, key = { it.id }) { helper ->
                    SearchHelperCard(
                        helper   = helper,
                        onBook   = { onBook(helper.id) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // ── Empty state ──────────────────────────────────────────────
                if (displayedHelpers.isEmpty()) {
                    item {
                        Box(
                            modifier         = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "No helpers found",
                                    color    = TextSecondary,
                                    fontSize = 16.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Try a different service or clear your search",
                                    color    = TextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Helper card ────────────────────────────────────────────────────────────────
@Composable
private fun SearchHelperCard(
    helper: HelperCard,
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HelperInitialsAvatar(
                initials = helper.initials,
                photoUrl = helper.photoUrl,
                isActive = helper.isActive
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = helper.name,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextPrimary,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f, fill = false)
                    )
                    if (helper.isActive) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ActiveDotColor.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
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
                                    "Active",
                                    fontSize   = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = ActiveDotColor
                                )
                            }
                        }
                    }
                }
                Text(
                    text     = helper.service,
                    fontSize = 13.sp,
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
                        text     = String.format("%.1f", helper.rating),
                        fontSize = 13.sp,
                        color    = TextSecondary
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Image(
                painter            = painterResource(R.drawable.btn_book),
                contentDescription = "Book",
                contentScale       = ContentScale.Fit,
                modifier           = Modifier
                    .height(30.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBook() }
            )
        }
    }
}

// ── Avatar ─────────────────────────────────────────────────────────────────────
@Composable
private fun HelperInitialsAvatar(initials: String, photoUrl: String?, isActive: Boolean = true) {
    Box(
        modifier = Modifier.size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl != null) {
            AsyncImage(
                model              = photoUrl,
                contentDescription = initials,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier         = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = initials,
                    color      = Color.White,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(ActiveDotColor)
                )
            }
        }
    }
}

// ── Chip ───────────────────────────────────────────────────────────────────────
@Composable
private fun FilterChipItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) ChipSelectedBg else ChipUnselBg)
            .border(
                width = 1.dp,
                color = if (isSelected) ChipSelectedBg else ChipUnselBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            color      = if (isSelected) ChipSelectedText else ChipUnselText,
            fontSize   = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
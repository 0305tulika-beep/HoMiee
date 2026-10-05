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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.viewmodel.navigation.HelperSearchViewModel
import com.example.homiee.viewmodel.navigation.SearchHelperUi

// ── Design tokens ──────────────────────────────────────────────────────────────
private val ChipSelectedBg   = Color(0xFFE0F2EF)
private val ChipSelectedText = Color(0xFF0F766E)
private val ChipUnselBg      = Color.White
private val ChipUnselBorder  = Color(0xFFCCCCCC)
private val ChipUnselText    = Color(0xFF333333)
private val TextPrimary      = Color(0xFF1A1A1A)
private val TextSecondary    = Color(0xFF7A7A7A)
private val StarColor        = Color(0xFFF4B400)
private val CardBg           = Color.White
private val ActiveDotColor   = Color(0xFF2ECC71)

// ── Data ───────────────────────────────────────────────────────────────────────
enum class SortOption(val label: String) {
    NEAREST("Nearest"),
    HIGHEST_RATED("Highest Rated"),
    LOWEST_COST("Lowest Cost")
}

private val SERVICE_FILTERS = listOf(
    "All", "Cleaning", "Cooking", "Eldercare", "Babysitting", "Laundry"
)

// ── Screen ─────────────────────────────────────────────────────────────────────
@Composable
fun SearchScreen(
    initialFilter: String = "All",
    onViewProfile: (String) -> Unit = {},
    onBook: (String) -> Unit = {},
    onNavItemClick: (String) -> Unit = {},
    viewModel: HelperSearchViewModel = viewModel()
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var searchQuery    by remember { mutableStateOf("") }
    var selectedSort   by remember { mutableStateOf(SortOption.NEAREST) }
    var selectedFilter by remember { mutableStateOf(initialFilter) }

    val state by viewModel.uiState.collectAsState()

    // Apply the initial category once; later changes go through the chip onClick
    LaunchedEffect(Unit) { viewModel.setFilter(initialFilter) }

    // Text matching is done by the server; only sorting stays local.
    val displayedHelpers = remember(state.helpers, selectedSort) {
        when (selectedSort) {
            SortOption.NEAREST       -> state.helpers.sortedBy { it.distanceKm ?: Double.MAX_VALUE }
            SortOption.HIGHEST_RATED -> state.helpers.sortedByDescending { it.rating }
            SortOption.LOWEST_COST   -> state.helpers.sortedBy { it.minPrice ?: Double.MAX_VALUE }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                painter            = painterResource(R.drawable.bg1),
                contentDescription = null,
                contentScale       = ContentScale.FillBounds,
                modifier           = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Fixed: header + search bar ─────────────────────────────
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
                            onValueChange = {
                                searchQuery = it
                                viewModel.setQuery(it)
                            },
                            placeholder   = {
                                Text(
                                    "Search by name or service...",
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
                                cursorColor             = GreenDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ── Fixed: service filter chips ────────────────────────────
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SERVICE_FILTERS) { filter ->
                        FilterChipItem(
                            label      = filter,
                            isSelected = selectedFilter.equals(filter, ignoreCase = true),
                            onClick    = {
                                selectedFilter = filter
                                viewModel.setFilter(filter)
                            }
                        )
                    }
                }

                // ── Fixed: sort by ─────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text          = "SORT BY",
                        fontSize      = 13.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = TextPrimary,
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

                // ── Scrollable: results ────────────────────────────────────
                LazyColumn(
                    modifier       = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    when {
                        state.isLoading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 60.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = GreenDark)
                                }
                            }
                        }

                        state.errorMessage != null -> {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 60.dp, start = 24.dp, end = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text     = state.errorMessage ?: "",
                                        color    = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    TextButton(onClick = { viewModel.retry() }) {
                                        Text(
                                            "Retry",
                                            color      = GreenDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            item {
                                Text(
                                    text       = "${displayedHelpers.size} HELPERS FOUND",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = TextPrimary,
                                    modifier   = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }

                            items(displayedHelpers, key = { it.id }) { helper ->
                                SearchHelperCard(
                                    helper   = helper,
                                    onClick  = { onViewProfile(helper.id) },
                                    onBook   = { onBook(helper.id) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }

                            if (displayedHelpers.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
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
        }
    }
}

// ── Helper card ────────────────────────────────────────────────────────────────
@Composable
private fun SearchHelperCard(
    helper: SearchHelperUi,
    onClick: () -> Unit,
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                initials = helper.name.take(2).uppercase(),
                photoUrl = helper.photoUrl,
                isActive = true
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = helper.name,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = TextPrimary,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Text(
                    text     = helper.service,
                    fontSize = 13.sp,
                    color    = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    helper.distanceKm?.let { km ->
                        Text(
                            text     = "  •  ${String.format("%.1f", km)} km",
                            fontSize = 13.sp,
                            color    = TextSecondary
                        )
                    }
                }
                helper.minPrice?.let { price ->
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text       = "From ₹${price.toInt()}/hr",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color      = GreenDark
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .height(34.dp)
                    .widthIn(min = 84.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(GreenDark)
                    .clickable { onBook() }
                    .padding(horizontal = 14.dp),
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
                placeholder        = painterResource(R.drawable.ic_profile_placeholder),
                error              = painterResource(R.drawable.ic_profile_placeholder),
                modifier           = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier         = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(GreenDark),
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
                color = if (isSelected) Color(0xFFB2DFDB) else ChipUnselBorder,
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
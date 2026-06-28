package com.example.homiee.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.homiee.R

enum class NavTab { HOME, SEARCH, BOOKINGS, MESSAGE, ACCOUNT }

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()  // ← grows to fit content, not fixed 80dp
    ) {
//        Image(
//            painter      = painterResource(id = R.drawable.nvb),
//            contentDescription = null,
//            contentScale = ContentScale.FillBounds,
//            modifier     = Modifier.matchParentSize()  // ← stretches bg to match Box size
//        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),  // ← no systemBarsPadding()
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NavBarItem(R.drawable.nv1, NavTab.HOME,     selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv2, NavTab.SEARCH,   selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv3, NavTab.BOOKINGS, selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv5, NavTab.ACCOUNT,  selectedTab, onTabSelected)
        }
    }
}

@Composable
private fun NavBarItem(
    iconRes: Int,
    tab: NavTab,
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    Image(
        painter            = painterResource(id = iconRes),
        contentDescription = tab.name,
        contentScale       = ContentScale.Fit,
        modifier           = Modifier
            .size(52.dp)          // ← slightly bigger now that height isn't capped
            .clickable { onTabSelected(tab) }
    )
}
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Bottom Nav Bar")
@Composable
fun BottomNavBarPreview() {
    var selected by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(NavTab.HOME)
    }

    BottomNavBar(
        selectedTab = selected,
        onTabSelected = { selected = it }
    )
}
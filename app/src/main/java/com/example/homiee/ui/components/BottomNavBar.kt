package com.example.homiee.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.example.homiee.R

enum class NavTab { HOME, SEARCH, BOOKINGS, MESSAGE, ACCOUNT }

private val GreenSelected   = Color(0xFF1A5C3A)
private val GreyUnselected  = Color(0xFF7A7A7A)

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NavBarItem(R.drawable.nv1, NavTab.HOME,     selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv2, NavTab.SEARCH,   selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv3, NavTab.BOOKINGS, selectedTab, onTabSelected)
            NavBarItem(R.drawable.nv4, NavTab.MESSAGE,  selectedTab, onTabSelected)
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
        colorFilter        = ColorFilter.tint(
            if (tab == selectedTab) GreenSelected else GreyUnselected
        ),
        modifier           = Modifier
            .size(52.dp)
            .clickable { onTabSelected(tab) }
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Bottom Nav Bar")
@Composable
fun BottomNavBarPreview() {
    var selected by remember { mutableStateOf(NavTab.HOME) }
    BottomNavBar(
        selectedTab    = selected,
        onTabSelected  = { selected = it }
    )
}
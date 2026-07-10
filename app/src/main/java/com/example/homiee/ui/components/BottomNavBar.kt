package com.example.homiee.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.theme.GreenDark

enum class NavTab { HOME, SEARCH, BOOKINGS, MESSAGE, ACCOUNT }

//private val GreenSelected  = Color(0xFF1A5C3A)
private val GreenSoftBg    = Color(0xFFE3EFE8)
private val GreyUnselected = Color(0xFF9A9A9A)

private data class NavItemData(
    val tab: NavTab,
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
)

private val navItems = listOf(
    NavItemData(NavTab.HOME,     "Home",     Icons.Filled.Home,          Icons.Outlined.Home),
    NavItemData(NavTab.SEARCH,   "Search",   Icons.Filled.Search,        Icons.Outlined.Search),
    NavItemData(NavTab.BOOKINGS, "Bookings", Icons.Filled.EventNote,     Icons.Outlined.EventNote),
    NavItemData(NavTab.MESSAGE,  "Messages", Icons.Filled.ChatBubble,    Icons.Outlined.ChatBubbleOutline),
    NavItemData(NavTab.ACCOUNT,  "Account",  Icons.Filled.Person,        Icons.Outlined.Person)
)

@Composable
fun BottomNavBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                NavBarItem(
                    item       = item,
                    isSelected = item.tab == selectedTab,
                    onClick    = { onTabSelected(item.tab) }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavItemData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Pops bigger when selected, springs back naturally
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 30.dp else 24.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "iconSize"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) GreenSoftBg else Color.Transparent,
        label = "bgColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) GreenDark else GreyUnselected,
        label = "contentColor"
    )

    val pillWidth by animateDpAsState(
        targetValue = if (isSelected) 64.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "pillWidth"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
//                .width(pillWidth.coerceAtLeast(iconSize + 16.dp))
                .height(38.dp)
                .background(bgColor, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = if (isSelected) item.filledIcon else item.outlinedIcon,
                contentDescription = item.label,
                tint               = contentColor,
                modifier           = Modifier.size(iconSize)
            )
        }
//        Spacer(Modifier.height(2.dp))
        Text(
            text       = item.label,
            fontSize   = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color      = contentColor
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "Bottom Nav Bar")
@Composable
fun BottomNavBarPreview() {
    var selected by remember { mutableStateOf(NavTab.HOME) }
    BottomNavBar(
        selectedTab   = selected,
        onTabSelected = { selected = it }
    )
}
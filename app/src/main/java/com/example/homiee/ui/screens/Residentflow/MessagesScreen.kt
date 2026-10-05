package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark

private val GreenPrimary  = Color(0xFF1A5C3A)
private val AvatarGreen  = Color(0xFF2E7D67)
private val GreenTint     = Color(0xFFE6F1EB)
private val OnlineGreen   = Color(0xFF4CAF50)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val Divider       = Color(0xFFF0F0F0)

data class MessageThread(
    val id:           String,
    val helperName:   String,
    val service:      String,
    val lastMessage:  String,
    val time:         String,
    val unreadCount:  Int = 0,
    val isOnline:     Boolean = false,
    val initials:     String = helperName.take(2).uppercase()
)

private val MOCK_THREADS = listOf(
    MessageThread("t001", "Ramesh Kumar", "Cleaning",  "I'm on my way, will reach by 10!", "8:02 AM", unreadCount = 2, isOnline = true),
    MessageThread("t002", "Sunita Devi",  "Cooking",   "I'm on my way, will reach by 10!", "8:01 AM", isOnline = true),
    MessageThread("t003", "Priya Singh",  "Eldercare", "I'm on my way, will reach by 10!", "8:02 AM"),
    MessageThread("t004", "Anita Rao",    "Cleaning",  "I'm on my way, will reach by 10!", "8:02 AM"),
    MessageThread("t005", "Kavita Singh", "Babysit",   "I'm on my way, will reach by 10!", "8:02 AM"),
)

@Composable
fun MessagesScreen(
    onNavItemClick: (String) -> Unit = {},
    onThreadClick:  (String) -> Unit = {},
    onNewMessageClick: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.White,
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.MESSAGE,
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
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Header ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GreenDark, AvatarGreen)))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text          = "Messages",
                        fontSize      = 28.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text       = "Stay connected with your helpers and manage your conversations.",
                        fontSize   = 14.sp,
                        lineHeight = 20.sp,
                        color      = Color.White.copy(alpha = 0.9f)
                    )
                }

                Spacer(Modifier.width(16.dp))

                // New message button: translucent circle + vector icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f))
                        .clickable { onNewMessageClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.AddComment,
                        contentDescription = "New message",
                        tint               = Color.White,
                        modifier           = Modifier.size(24.dp)
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(MOCK_THREADS, key = { it.id }) { thread ->
                    MessageThreadItem(
                        thread  = thread,
                        onClick = { onThreadClick(thread.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageThreadItem(
    thread:  MessageThread,
    onClick: () -> Unit
) {
    val hasUnread = thread.unreadCount > 0

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Avatar with online dot ──
            Box(modifier = Modifier.size(50.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(GreenTint),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = thread.initials,
                        color      = GreenPrimary,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (thread.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(OnlineGreen)
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            // ── Name, service, preview ──
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = thread.helperName,
                    fontSize   = 15.sp,
                    fontWeight = if (hasUnread) FontWeight.Bold else FontWeight.SemiBold,
                    color      = TextPrimary,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Text(
                    text       = thread.service,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color      = GreenPrimary
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text       = thread.lastMessage,
                    fontSize   = 13.sp,
                    color      = if (hasUnread) TextPrimary else TextSecondary,
                    fontWeight = if (hasUnread) FontWeight.Medium else FontWeight.Normal,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(10.dp))

            // ── Time + unread badge ──
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = thread.time,
                    fontSize   = 11.sp,
                    color      = if (hasUnread) GreenPrimary else TextSecondary,
                    fontWeight = if (hasUnread) FontWeight.SemiBold else FontWeight.Normal
                )
                if (hasUnread) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
                            .clip(CircleShape)
                            .background(GreenDark)
                            .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = thread.unreadCount.toString(),
                            color      = Color.White,
                            fontSize   = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            color     = Divider,
            thickness = 0.5.dp,
            modifier  = Modifier.padding(start = 84.dp, end = 20.dp)
        )
    }
}
package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.navigation.Routes
import com.example.homiee.navigation.toRoute
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenDarkk

private val GreenPrimary  = Color(0xFF1A5C3A)
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)

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
    MessageThread("t001", "Ramesh Kumar", "Cleaning", "I'm on my way, will reach by 10!", "8:02 AM", unreadCount = 2, isOnline = true),
    MessageThread("t002", "Sunita Devi",  "Cooking",  "I'm on my way, will reach by 10!", "8:01 AM", isOnline = true),
    MessageThread("t003", "Priya Singh",  "Laundry",  "I'm on my way, will reach by 10!", "8:02 AM"),
    MessageThread("t004", "Anita Rao",    "Cleaning", "I'm on my way, will reach by 10!", "8:02 AM"),
    MessageThread("t005", "Kavita Singh", "Babysit",  "I'm on my way, will reach by 10!", "8:02 AM"),
)

@Composable
fun MessagesScreen(
    onNavItemClick: (String) -> Unit = {},
    onThreadClick:  (String) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.MESSAGE,
                onTabSelected = { onNavItemClick(it.toRoute()) }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F7F7))
        ) {

            // ── Green header ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenDark)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text       = "Messages",
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }

            // ── Thread list ──
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Avatar with online dot ──
        Box(modifier = Modifier.size(48.dp)) {
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = thread.initials,
                    color      = Color.White,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (thread.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // ── Name + preview ──
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = thread.helperName,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
            Text(
                text     = thread.service,
                fontSize = 11.sp,
                color    = GreenPrimary
            )
            Text(
                text     = thread.lastMessage,
                fontSize = 12.sp,
                color    = TextSecondary,
                maxLines = 1
            )
        }

        Spacer(Modifier.width(8.dp))

        // ── Time + unread badge ──
        Column(horizontalAlignment = Alignment.End) {
            Text(thread.time, fontSize = 11.sp, color = TextSecondary)
            if (thread.unreadCount > 0) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier         = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(GreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = thread.unreadCount.toString(),
                        color    = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    HorizontalDivider(
        color     = Color(0xFFF0F0F0),
        thickness = 0.5.dp,
        modifier  = Modifier.padding(start = 76.dp)
    )
}
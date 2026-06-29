package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenDarkk
import kotlinx.coroutines.launch

private val GreenPrimary    = Color(0xFF1A5C3A)
private val GreenBubble     = Color(0xFF1A5C3A)
private val GreyBubble      = Color(0xFFF0F0F0)
private val TextPrimary     = Color(0xFF1A1A1A)
private val TextSecondary   = Color(0xFF7A7A7A)

data class ChatMessage(
    val id:       String,
    val text:     String,
    val isMe:     Boolean,
    val time:     String
)

private val MOCK_MESSAGES = listOf(
    ChatMessage("m1", "Hi! I've confirmed your booking for cleaning tomorrow.", false, "9:00 AM"),
    ChatMessage("m2", "Great! What time will you arrive?",                      true,  "9:01 AM"),
    ChatMessage("m3", "I'll be there by 10:00 AM sharp.",                       false, "9:02 AM"),
    ChatMessage("m4", "jdfgaojf bhaopfla tqbehtvuyteortv /Luasatgcnyc lhger glrngvg glhjgfgr", false, "9:02 AM"),
    ChatMessage("m5", "nrfu/jahneaogftayvrtoe bYWY1 U GJKGG aoa hdplg u agffjqquiakajr ni qpjk/qnjknoqjlqh -bnhhjqjc", true, "9:03 AM"),
)

@Composable
fun ChatScreen(
    threadId:   String  = "",
    helperName: String  = "Ramesh Kumar",
    service:    String  = "Cleaning",
    onBack:     () -> Unit = {},
    onViewBooking: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var messageText by remember { mutableStateOf("") }
    val messages    = remember { mutableStateListOf(*MOCK_MESSAGES.toTypedArray()) }
    val listState   = rememberLazyListState()
    val scope       = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {

        // ── Green header ──
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .background(GreenDark)
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = Color.White,
                    modifier           = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(10.dp))

            // ── Avatar ──
            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = helperName.take(2).uppercase(),
                    color      = Color.White,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = helperName,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }
        }

        // ── Messages ──
        LazyColumn(
            modifier       = Modifier.weight(1f).padding(horizontal = 12.dp),
            state          = listState,
            contentPadding = PaddingValues(vertical = 12.dp),
            reverseLayout  = false
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
                Spacer(Modifier.height(6.dp))
            }
        }

        // ── Input bar ──
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (messageText.isEmpty()) {
                    Text("Type a message...", fontSize = 14.sp, color = TextSecondary)
                }
                BasicTextField(
                    value         = messageText,
                    onValueChange = { messageText = it },
                    textStyle     = TextStyle(fontSize = 14.sp, color = TextPrimary),
                    modifier      = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
                    .clickable {
                        if (messageText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id    = "m${messages.size + 1}",
                                    text  = messageText,
                                    isMe  = true,
                                    time  = "Now"
                                )
                            )
                            messageText = ""
                            scope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Send,
                    contentDescription = "Send",
                    tint               = Color.White,
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isMe) {
            Box(
                modifier         = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text("RK", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(6.dp))
        }

        Column(horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart     = if (message.isMe) 16.dp else 4.dp,
                            topEnd       = if (message.isMe) 4.dp  else 16.dp,
                            bottomStart  = 16.dp,
                            bottomEnd    = 16.dp
                        )
                    )
                    .background(if (message.isMe) GreenBubble else GreyBubble)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text      = message.text,
                    fontSize  = 13.sp,
                    color     = if (message.isMe) Color.White else TextPrimary,
                    lineHeight = 18.sp
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(message.time, fontSize = 10.sp, color = TextSecondary)
        }
    }
}
package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.theme.GreenDark
import kotlinx.coroutines.delay

private val GreenPrimary  = GreenDark
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)

@Composable
fun ActivityScreen(
    bookingId:  String = "",
    helperName: String = "Ramesh Kumar",
    onBack:     () -> Unit = {},
    onChat:     () -> Unit = {},
    onCall:     () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    // ── Elapsed timer ──
    var elapsedSeconds by remember { mutableStateOf(48 * 60 + 12) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            elapsedSeconds++
        }
    }
    val hours   = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60
    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {

        // ── Green header ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreenPrimary)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = "ACTIVE JOB",
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                    Text(
                        text     = helperName,
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── STATUS card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "STATUS",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenPrimary
                    )
                    Spacer(Modifier.height(12.dp))

                    // ── Timer box ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GreenPrimary)
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text     = "Time elapsed",
                                fontSize = 12.sp,
                                color    = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text       = timeString,
                                fontSize   = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White
                            )
                            Text(
                                text     = "Estimated end: 11:00 AM",
                                fontSize = 12.sp,
                                color    = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Progress steps ──
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        // On The Way — completed
                        ProgressStep(label = "On The Way", isDone = true, isActive = false)

                        // Line
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .padding(horizontal = 4.dp)
                                .background(GreenPrimary)
                        )

                        // Started — active
                        ProgressStep(label = "Started", isDone = false, isActive = true)

                        // Line
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .padding(horizontal = 4.dp)
                                .background(Color(0xFFDDDDDD))
                        )

                        // Completed — pending
                        ProgressStep(label = "Completed", isDone = false, isActive = false)
                    }
                }
            }

            // ── Helper info card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier         = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = helperName.take(2).uppercase(),
                            color      = Color.White,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(helperName, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                        Text("Mother +91 xxxxxxxxx", fontSize = 12.sp, color = TextSecondary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GreenLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Verified", fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // ── Call + Chat buttons ──
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick  = onCall,
                    shape    = RoundedCornerShape(12.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Call", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                OutlinedButton(
                    onClick  = onChat,
                    shape    = RoundedCornerShape(12.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Chat", color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            // ── SAFETY card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text       = "SAFETY",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = GreenPrimary
                    )
                    Spacer(Modifier.height(10.dp))

                    SafetyItem(label = "Share live status")
                    Spacer(Modifier.height(8.dp))
                    SafetyItem(label = "Report an issue")
                }
            }

            // ── Emergency SOS ──
            Button(
                onClick  = { },
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text       = "Emergency Support",
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ProgressStep(label: String, isDone: Boolean, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone   -> GreenDark
                        isActive -> GreenDark
                        else     -> Color(0xFFDDDDDD)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone || isActive) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = if (isDone || isActive) GreenDark else Color(0xFFAAAAAA))
    }
}

@Composable
private fun SafetyItem(label: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7))
            .clickable { }
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF1A1A1A))
    }
}
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

private val GreenPrimary  = GreenDark
private val GreenLight    = Color(0xFFE8F5EE)
private val TextPrimary   = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF7A7A7A)
private val StarSelected  = Color(0xFFFFC107)
private val StarEmpty     = Color(0xFFDDDDDD)

private val FEEDBACK_TAGS = listOf("Professional", "Punctual", "Friendly", "Skilled")

@Composable
fun FeedbackScreen(
    helperName: String = "Ramesh Kumar",
    bookingId:  String = "",
    onBack:     () -> Unit = {},
    onSubmit:   () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

    var selectedRating  by remember { mutableStateOf(0) }
    var selectedTags    by remember { mutableStateOf(setOf<String>()) }
    var writtenFeedback by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {

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
                        text       = "Rate Your Experience",
                        fontSize   = 20.sp,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Main feedback card ──
            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ── Avatar ──
                    Box(
                        modifier         = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = helperName.take(2).uppercase(),
                            color      = Color.White,
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text     = "How was ${helperName.split(" ").first()}'s service?",
                        fontSize = 15.sp,
                        color    = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(12.dp))

                    // ── Star rating ──
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(5) { index ->
                            Text(
                                text     = if (index < selectedRating) "★" else "☆",
                                fontSize = 36.sp,
                                color    = if (index < selectedRating) StarSelected else StarEmpty,
                                modifier = Modifier.clickable { selectedRating = index + 1 }
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(16.dp))

                    // ── Optional tags ──
                    Column(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text       = "OPTIONAL TAGS",
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color      = GreenPrimary
                        )
                        Spacer(Modifier.height(10.dp))
                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement   = Arrangement.spacedBy(8.dp)
                        ) {
                            FEEDBACK_TAGS.forEach { tag ->
                                val isSelected = tag in selectedTags
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isSelected) GreenPrimary else Color.White)
                                        .clickable {
                                            selectedTags = if (isSelected)
                                                selectedTags - tag
                                            else
                                                selectedTags + tag
                                        }
                                        .then(
                                            if (!isSelected) Modifier.then(
                                                Modifier.background(Color.White)
                                            ) else Modifier
                                        )
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text  = tag,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(16.dp))

                    // ── Written feedback ──
                    Column(
                        modifier            = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text       = "WRITTEN FEEDBACK",
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color      = GreenPrimary
                        )
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value         = writtenFeedback,
                            onValueChange = { writtenFeedback = it },
                            placeholder   = {
                                Text(
                                    "Share details about your experience....",
                                    fontSize = 13.sp,
                                    color    = TextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape    = RoundedCornerShape(12.dp),
                            colors   = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = GreenPrimary,
                                unfocusedBorderColor = Color(0xFFDDDDDD)
                            )
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Submit button ──
                    Button(
                        onClick  = onSubmit,
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled  = selectedRating > 0
                    ) {
                        Text(
                            text       = "Submit review",
                            color      = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
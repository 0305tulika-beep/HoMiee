package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.CardTextField
import com.example.homiee.ui.components.SectionLabel
import com.example.homiee.ui.components.SelectableChip
import com.example.homiee.ui.theme.BorderColor
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White

/**
 * STEP 3 — SCHEDULE
 * Work Type (text input), Time Slot chips, Day chips.
 *
 * HOW IT WORKS:
 * - Time slot and day selections stored as separate Set<String>
 * - Days use compact square chips with 2-letter labels
 */
@Composable
fun ResFormScheduleScreen(onNext: () -> Unit) {
    var workType      by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf(setOf("Morning")) }
    var selectedDays  by remember { mutableStateOf(setOf("MON", "WED", "FRI")) }

    val timeSlots = listOf("Morning", "Afternoon", "Evening")
    val days      = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

    ResFormShell(title = "Schedule", step = 3, onNext = onNext) {
        CardTextField(workType, { workType = it }, "e.g. Full-time / Part-time", label = "Work Type")

        Column {
            SectionLabel("Preferred Time Slots")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                timeSlots.forEach { slot ->
                    SelectableChip(
                        label    = slot,
                        selected = slot in selectedSlots,
                        onToggle = {
                            selectedSlots = if (slot in selectedSlots)
                                selectedSlots - slot else selectedSlots + slot
                        }
                    )
                }
            }
        }

        Column {
            SectionLabel("Day Required")
            Spacer(Modifier.height(8.dp))
            // Row 1: Mon–Fri
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                days.take(5).forEach { day ->
                    DayChip(day, day in selectedDays) {
                        selectedDays = if (day in selectedDays)
                            selectedDays - day else selectedDays + day
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            // Row 2: Sat–Sun
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                days.drop(5).forEach { day ->
                    DayChip(day, day in selectedDays) {
                        selectedDays = if (day in selectedDays)
                            selectedDays - day else selectedDays + day
                    }
                }
            }
        }
    }
}

@Composable
private fun DayChip(label: String, selected: Boolean, onToggle: () -> Unit) {
    val bg     = if (selected) GreenLight else Color.Transparent
    val text   = if (selected) White      else TextPrimary
    val border = if (selected) GreenLight else BorderColor

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.5.dp, border, RoundedCornerShape(8.dp))
            .clickable { onToggle() }
    ) {
        Text(label, color = text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
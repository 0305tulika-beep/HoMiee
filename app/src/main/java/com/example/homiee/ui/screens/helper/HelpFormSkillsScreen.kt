package com.example.homiee.ui.screens.helper

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.homiee.ui.components.CardTextField
import com.example.homiee.ui.components.SectionLabel
import com.example.homiee.ui.components.SelectableChip
import com.example.homiee.ui.theme.BorderColor
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.GreenTint

/**
 * STEP 3 — EXPERIENCE & SKILLS
 * Years of experience dropdown, previous work reference, language chips.
 *
 * HOW TO MAKE A DROPDOWN:
 * - Use ExposedDropdownMenuBox from Material3
 * - expanded state drives the menu visibility
 * - DropdownMenuItem items call onYearsSelected(it)
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFormSkillsScreen(onNext: () -> Unit) {
    val experienceOptions = listOf("< 1 year", "1–2 years", "3–5 years", "5+ years")
    var selectedExperience by remember { mutableStateOf("") }
    var dropdownExpanded   by remember { mutableStateOf(false) }
    var workReference      by remember { mutableStateOf("") }

    val languages = listOf("ENGLISH", "HINDI", "BENGALI", "TELUGU", "MARATHI", "TAMIL")
    var selectedLangs by remember { mutableStateOf(setOf("ENGLISH", "MARATHI")) }

    HelpFormShell(title = "Experience & Skills", step = 3, onNext = onNext) {
        // Experience dropdown
        Column {
            SectionLabel("YEARS OF EXPERIENCE")
            Spacer(Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded         = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value         = selectedExperience.ifEmpty { "Select experience" },
                    onValueChange = {},
                    readOnly      = true,
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = GreenTint,
                        unfocusedContainerColor = GreenTint,
                        focusedBorderColor      = GreenLight,
                        unfocusedBorderColor    = BorderColor
                    ),
                    shape    = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded         = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    experienceOptions.forEach { option ->
                        DropdownMenuItem(
                            text    = { Text(option) },
                            onClick = {
                                selectedExperience = option
                                dropdownExpanded   = false
                            }
                        )
                    }
                }
            }
        }

        CardTextField(
            value         = workReference,
            onValueChange = { workReference = it },
            placeholder   = "Enter reference name/contact",
            label         = "PREVIOUS WORK REFERENCE (OPTIONAL)"
        )

        Divider(color = Color(0xFFE0E0E0))

        Column {
            SectionLabel("LANGUAGES SPOKEN")
            Spacer(Modifier.height(8.dp))
            languages.chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    row.forEach { lang ->
                        SelectableChip(
                            label    = lang,
                            selected = lang in selectedLangs,
                            onToggle = {
                                selectedLangs = if (lang in selectedLangs)
                                    selectedLangs - lang else selectedLangs + lang
                            }
                        )
                    }
                }
            }
        }
    }
}
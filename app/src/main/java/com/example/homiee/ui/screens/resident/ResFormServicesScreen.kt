package com.example.homiee.ui.screens.resident

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.homiee.ui.components.SelectableChip

/**
 * STEP 2 — TYPE OF HELP NEEDED
 * Multi-select chip grid. Selected chips show filled teal.
 *
 * HOW IT WORKS:
 * - `selectedServices` is a Set<String> held in mutableStateOf
 * - Tapping a chip toggles it in/out of the set
 */
@Composable
fun ResFormServicesScreen(onNext: () -> Unit) {
    val allServices = listOf(
        "House cleaning", "Cooking", "Laundry", "Babysitting",
        "Cleaning", "Home Organizing", "Dishwashing", "Deep Cleaning", "OTHER"
    )
    var selectedServices by remember { mutableStateOf(setOf("House cleaning", "Cooking")) }

    ResFormShell(title = "Type Of Help Needed", step = 2, onNext = onNext) {
        allServices.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { service ->
                    SelectableChip(
                        label    = service,
                        selected = service in selectedServices,
                        onToggle = {
                            selectedServices = if (service in selectedServices)
                                selectedServices - service
                            else
                                selectedServices + service
                        },
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }
}
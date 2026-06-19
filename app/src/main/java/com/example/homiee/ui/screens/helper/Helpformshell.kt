package com.example.homiee.ui.screens.helper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.StepProgressBar
import com.example.homiee.ui.theme.White

/**
 * Shared layout wrapper used by ALL 4 helper form screens.
 *
 *  - bg3.png fills the entire screen as the background
 *  - bgw.png is layered on top, lower down, acting as the white card
 *  - Header height shrunk to just fit the step bar + title
 */
@Composable
fun HelpFormShell(
    title: String,
    step: Int,
    totalSteps: Int = 4,
    onNext: () -> Unit,
    nextLabel: String = "Next",
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Full-screen background image (bg3.png) ──────────────────
        Image(
            painter            = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Just enough space for the step bar + title to show above the card
            Spacer(modifier = Modifier.height(110.dp))

            // ── Card image (bgw.png) with content layered on top ────
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter            = painterResource(id = R.drawable.bgw),
                    contentDescription = null,
                    contentScale       = ContentScale.FillBounds,
                    modifier           = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        content()
                    }
                    Spacer(Modifier.height(24.dp))
                    HomieeButton(text = nextLabel, onClick = onNext)
                }
            }
        }

        // ── Title + step progress bar on top of bg3.png header ──────
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            StepProgressBar(totalSteps = totalSteps, currentStep = step)
            Spacer(Modifier.height(12.dp))
            Text(
                text       = title,
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold,
                color      = White
            )
        }
    }
}
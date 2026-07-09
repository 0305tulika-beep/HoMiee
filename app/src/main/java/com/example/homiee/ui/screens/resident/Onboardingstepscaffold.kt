package com.example.homiee.ui.screens.resident

import com.example.homiee.ui.components.OnboardingStepIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.HomieeColors
import com.example.homiee.ui.components.HomieeHeader

val HELPER_ONBOARDING_STEP_LABELS = listOf(
    "Address\n",
    "Emergency\n Contact",
    "Identity\n Verification",
    "Profile\n Photo"
)
@Composable
fun OnboardingStepScaffold(
    currentStep: Int,
    stepLabels: List<String> = HELPER_ONBOARDING_STEP_LABELS,
    title: String,
    subtitle: String,
    buttonText: String,
    buttonIcon: ImageVector = Icons.Default.ArrowForward,
    onButtonClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = { HomieeHeader() },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HomieeColors.PrimaryDark)
                ) {
                    Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(buttonIcon, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = HomieeColors.TextGray
            )
            Spacer(modifier = Modifier.height(28.dp))
            OnboardingStepIndicator(currentStep = currentStep, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(32.dp))
            content()
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
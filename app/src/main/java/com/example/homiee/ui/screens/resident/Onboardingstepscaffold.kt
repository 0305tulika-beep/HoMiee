package com.example.homiee.ui.screens.resident

import com.example.homiee.ui.components.OnboardingStepIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.HideSystemBars2
import com.example.homiee.ui.components.HomieeColors
import com.example.homiee.ui.components.HomieeHeader
import com.example.homiee.ui.theme.GreenDark

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
    onBackClick: (() -> Unit)? = null,
    isLoading: Boolean = false,          // NEW
    errorMessage: String? = null,        // NEW
    content: @Composable ColumnScope.() -> Unit
) {
    HideSystemBars2(lightIcons = true)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { HomieeHeader() },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                // NEW: warning / error banner, shown above the buttons
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFDC2626),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                if (onBackClick != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBackClick,
                            enabled = !isLoading,   // NEW
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = HomieeColors.PrimaryDark
                            )
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Previous", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = GreenDark)
                        }

                        Button(
                            onClick = onButtonClick,
                            enabled = !isLoading,   // NEW
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HomieeColors.PrimaryDark)
                        ) {
                            if (isLoading) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(buttonIcon, contentDescription = null)
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = onButtonClick,
                        enabled = !isLoading,   // NEW
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HomieeColors.PrimaryDark)
                    ) {
                        if (isLoading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(buttonIcon, contentDescription = null)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = title, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF111827))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = subtitle, fontSize = 14.sp, color = HomieeColors.TextGray)
            Spacer(modifier = Modifier.height(28.dp))
            OnboardingStepIndicator(currentStep = currentStep, stepLabels = stepLabels)
            Spacer(modifier = Modifier.height(32.dp))
            content()
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
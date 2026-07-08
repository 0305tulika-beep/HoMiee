package com.example.homiee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.homiee.ui.theme.*

@Composable
fun HomieeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = GreenDark
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(50),
        colors   = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text       = text,
            style      = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
object HomieeColors {
    val PrimaryDark = Color(0xFF14532D)
    val PrimaryMint = Color(0xFFD9F2E6)
    val TextGray = Color(0xFF6B7280)
    val BorderGray = Color(0xFFE5E7EB)
    val StepInactive = Color(0xFFE5E7EB)
    val StepInactiveText = Color(0xFF9CA3AF)
}
@Composable
fun GradientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        placeholder   = { Text(placeholder, color = White.copy(alpha = 0.7f)) },
        singleLine    = true,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon  = if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = White.copy(alpha = 0.7f)
                    )
                }
            }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = White,
            unfocusedBorderColor = White.copy(alpha = 0.5f),
            focusedTextColor     = White,
            unfocusedTextColor   = White,
            cursorColor          = White,
            focusedContainerColor   = White.copy(alpha = 0.15f),
            unfocusedContainerColor = White.copy(alpha = 0.1f),
        ),
        shape    = RoundedCornerShape(50),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun HideSystemBars() {
    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as android.app.Activity).window
        val controller = WindowInsetsControllerCompat(window, view)

        // Make nav bar + status bar transparent and draw content behind them
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        // Light icons since your background is dark/green
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        onDispose { }
    }
}

@Composable
fun TransparentStatusBarWhiteNavBar(lightStatusBarIcons: Boolean = true) {
    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as android.app.Activity).window
        val controller = WindowInsetsControllerCompat(window, view)

        // Let content draw behind both system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Status bar: transparent, icon color depends on what's behind it
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        controller.isAppearanceLightStatusBars = !lightStatusBarIcons

        // Nav bar: solid white, matching BottomNavBar's nvb.png background
        window.navigationBarColor = android.graphics.Color.WHITE
        controller.isAppearanceLightNavigationBars = true // dark icons on white bg

        onDispose { }
    }
}
@Composable
fun systemBarsPadding(): PaddingValues {
    return WindowInsets.systemBars.asPaddingValues()
}

@Composable
fun OnboardingStepIndicator(
    currentStep: Int,
    stepLabels: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stepLabels.forEachIndexed { index, label ->
            val stepNumber = index + 1
            val isActiveOrDone = stepNumber <= currentStep
            val isActive = stepNumber == currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(if (isActive) 44.dp else 40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActiveOrDone) HomieeColors.PrimaryDark
                            else HomieeColors.StepInactive
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber.toString(),
                        color = if (isActiveOrDone) Color.White else HomieeColors.StepInactiveText,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isActive) 18.sp else 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) HomieeColors.PrimaryDark else HomieeColors.TextGray,
                    textAlign = TextAlign.Center
                )
            }

            if (index != stepLabels.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 20.dp)
                        .height(2.dp)
                        .background(
                            if (stepNumber < currentStep) HomieeColors.PrimaryDark
                            else HomieeColors.StepInactive
                        )
                )
            }
        }
    }
}

@Composable
fun OtpBox(
    digit: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .border(2.dp, GreenDark, CircleShape)
    ) {
        Text(
            text       = digit,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
    }
}
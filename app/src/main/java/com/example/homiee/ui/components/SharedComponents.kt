

package com.example.homiee.ui.components

import androidx.compose.foundation.background

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
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
        colors   = ButtonDefaults.buttonColors(containerColor = containerColor),  // ← use it here
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

//text field
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
fun HideSystemBars(lightIcons: Boolean) {
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
fun CardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text       = label,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary,
                modifier   = Modifier.padding(bottom = 4.dp)
            )
        }
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = TextMuted) },
            singleLine    = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = GreenLight,
                unfocusedBorderColor = BorderColor,
                focusedContainerColor   = GreenTint,
                unfocusedContainerColor = GreenTint,
                focusedTextColor     = TextPrimary,
                unfocusedTextColor   = TextPrimary,
            ),
            shape    = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun systemBarsPadding(): PaddingValues {
    return WindowInsets.systemBars.asPaddingValues()
}
@Composable
fun SelectableChip(
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg     = if (selected) GreenLight else Color.Transparent
    val border = if (selected) GreenLight else BorderColor
    val text   = if (selected) White      else TextPrimary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(1.5.dp, border, RoundedCornerShape(50))
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(label, color = text, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}


@Composable
fun StepProgressBar(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            val active = index < currentStep
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (active) GreenLight else Color(0xFFCCDDD8))
            )
        }
    }
}


@Composable
fun OutlinedActionButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick  = onClick,
        shape    = RoundedCornerShape(12.dp),
        border   = ButtonDefaults.outlinedButtonBorder.copy(
            // teal border
        ),
        colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenMid),
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontWeight = FontWeight.Medium)
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


@Composable
fun FormCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape    = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors   = CardDefaults.cardColors(containerColor = White),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text       = text,
        style      = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color      = TextPrimary,
        letterSpacing = 0.5.sp
    )
}
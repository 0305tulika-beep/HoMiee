package com.example.homiee.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.homiee.ui.theme.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowInsetsCompat
import androidx.compose.foundation.Image
import com.example.homiee.R


@Composable
fun HomieeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = GreenDark
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressedColor = remember(containerColor) {
        Color(
            red   = containerColor.red   * 0.85f,
            green = containerColor.green * 0.85f,
            blue  = containerColor.blue  * 0.85f,
            alpha = containerColor.alpha
        )
    }

    Button(
        onClick  = onClick,
        enabled  = enabled,
        shape    = RoundedCornerShape(50),
        colors   = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) pressedColor else containerColor
        ),
        interactionSource = interactionSource,
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
fun HideSystemBars2(lightIcons: Boolean = false) {
    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as android.app.Activity).window
        val controller = WindowInsetsControllerCompat(window, view)

        // Draw edge-to-edge; content can extend behind the bars,
        // but we'll pad it manually using insets in the screen composables
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Make both bars fully transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        // Icon appearance (dark icons on light background, or vice versa)
        controller.isAppearanceLightStatusBars = !lightIcons
        controller.isAppearanceLightNavigationBars = !lightIcons

        // Ensure both bars are always visible (no hide/swipe behavior)
        controller.show(WindowInsetsCompat.Type.systemBars())

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
fun statusBarsPadding(): PaddingValues {
    return WindowInsets.statusBars.asPaddingValues()
}

@Composable
fun navigationBarsPadding(): PaddingValues {
    return WindowInsets.navigationBars.asPaddingValues()
}


@Composable
fun OtpBox(
    digit: String,
    hasError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val ErrorRed = Color(0xFFFF6B6B)

    // NEW: a little spring "pop" whenever a digit lands in this box —
    // snaps down to 70% scale then springs back past/to 100%, giving a
    // satisfying bounce as the user types each OTP digit. Purely visual:
    // graphicsLayer scales the rendered box without affecting its measured
    // layout size, so neighboring boxes in the OTP row never shift.
    val scale = remember { Animatable(1f) }
    var previousDigit by remember { mutableStateOf("") }

    LaunchedEffect(digit) {
        if (digit.isNotEmpty() && digit != previousDigit) {
            scale.snapTo(0.7f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
        previousDigit = digit
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .border(2.dp, if (hasError) ErrorRed else GreenDark, CircleShape)
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
fun HomieeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GreenDarkk, GreenDark, GreenLight),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 300f)
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.logotext),
            contentDescription = "HoMiee",
            modifier = Modifier.height(35.dp)
        )
    }
}

@Composable
fun HomieeFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int? = null   // NEW
) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // NEW: silently cap input length instead of letting it grow unbounded
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            placeholder = { Text(placeholder, color = HomieeColors.TextGray) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = HomieeColors.PrimaryDark) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GradientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    onFocusLost: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var wasFocused by remember { mutableStateOf(false) }
    val ErrorRed = Color(0xFFFF6B6B)

    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        placeholder   = { Text(placeholder, color = White.copy(alpha = 0.7f)) },
        singleLine    = true,
        isError       = isError,
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
            focusedBorderColor   = if (isError) ErrorRed else White,
            unfocusedBorderColor = if (isError) ErrorRed else White.copy(alpha = 0.5f),
            errorBorderColor     = ErrorRed,
            focusedTextColor     = White,
            unfocusedTextColor   = White,
            cursorColor          = White,
            focusedContainerColor   = White.copy(alpha = 0.15f),
            unfocusedContainerColor = White.copy(alpha = 0.1f),
        ),
        shape    = RoundedCornerShape(50),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                // Fires only when the field transitions FROM focused TO unfocused (i.e. user tapped away)
                if (wasFocused && !focusState.isFocused) {
                    onFocusLost()
                }
                wasFocused = focusState.isFocused
            }
    )
}
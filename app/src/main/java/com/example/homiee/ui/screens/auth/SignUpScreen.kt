package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.GradientTextField
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.statusBarsPadding
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.White
import com.example.homiee.viewmodel.RegisterViewModel
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.homiee.ui.theme.CardBg
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.viewmodel.RegisterUiState

private val ErrorRed = Color(0xFFFF6B6B)

// ── Validation helpers ──
private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email.trim())

private fun emailErrorMessage(email: String, touched: Boolean): String? {
    if (touched && email.isBlank()) return "Email is required"
    if (email.isNotBlank() && !isValidEmail(email)) return "Please enter a valid email address"
    return null
}

private fun passwordErrorMessage(password: String, touched: Boolean): String? {
    if (touched && password.isEmpty()) return "Password is required"
    if (password.isEmpty()) return null

    // CHANGED: check for spaces first, and exclude whitespace from the
    // "special character" check below — previously a password consisting
    // partly/entirely of spaces (e.g. "password ") could incorrectly pass
    // the special-character requirement, since isLetterOrDigit() is false
    // for whitespace too.
    val hasSpace     = password.any { it.isWhitespace() }
    val hasMinLength = password.length >= 8
    val hasUpper     = password.any { it.isUpperCase() }
    val hasSpecial   = password.any { !it.isLetterOrDigit() && !it.isWhitespace() }

    return when {
        hasSpace      -> "Password must not contain spaces"
        !hasMinLength -> "Password must be at least 8 characters"
        !hasUpper     -> "Password must include at least 1 capital letter"
        !hasSpecial   -> "Password must include at least 1 special symbol"
        else          -> null
    }
}

private fun requiredError(value: String, touched: Boolean, fieldLabel: String): String? =
    if (touched && value.isBlank()) "$fieldLabel is required" else null

@Composable
fun SignUpScreen(
    navController: NavController,
    // CHANGED: nullable + skipped in preview. RegisterViewModel constructs
    // AuthRepository() unconditionally, which touches RetrofitClient.authApi —
    // if that singleton builds real network/auth infrastructure at init time,
    // it can throw inside the Preview sandbox. LocalInspectionMode guards it.
    viewModel: RegisterViewModel? = if (LocalInspectionMode.current) null else viewModel(),
    onSignedUp: (String) -> Unit = {}
)  {
    var firstName   by remember { mutableStateOf("") }
    var lastName    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var agreed      by remember { mutableStateOf(false) }

    var firstNameTouched   by remember { mutableStateOf(false) }
    var lastNameTouched    by remember { mutableStateOf(false) }
    var emailTouched       by remember { mutableStateOf(false) }
    var passwordTouched    by remember { mutableStateOf(false) }
    var confirmPassTouched by remember { mutableStateOf(false) }

    // CHANGED: fall back to a default state when there's no real ViewModel (preview)
    val uiState by viewModel?.uiState?.collectAsState()
        ?: remember { mutableStateOf(RegisterUiState()) }

    val firstNameError = requiredError(firstName, firstNameTouched, "First name")
    val lastNameError  = requiredError(lastName,  lastNameTouched,  "Last name")
    val emailError     = emailErrorMessage(email, emailTouched)
    val passwordError  = passwordErrorMessage(password, passwordTouched)
    val confirmError = when {
        confirmPassTouched && confirmPass.isEmpty() -> "Please confirm your password"
        confirmPass.isNotEmpty() && confirmPass.any { it.isWhitespace() } -> "Password must not contain spaces"
        confirmPass.isNotEmpty() && confirmPass != password -> "Passwords do not match"
        else -> null
    }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && emailError == null &&
            password.isNotBlank() && passwordError == null &&
            confirmPass.isNotBlank() && confirmError == null

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Routes.otpRoute(viewModel?.registeredEmail ?: "", "signup"))
            viewModel?.resetState()
        }
    }

    HideSystemBars()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter            = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(statusBarsPadding())
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Account", fontSize = 37.sp, fontWeight = FontWeight.Bold, color = White)
            Text("Join HoMiee", color = White.copy(alpha = 0.85f), fontSize = 18.sp)

            Spacer(Modifier.height(32.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    GradientTextField(
                        value         = firstName,
                        onValueChange = { firstName = it },
                        placeholder   = "First Name",
                        isError       = firstNameError != null,
                        onFocusLost   = { firstNameTouched = true }
                    )
                    if (firstNameError != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(firstNameError, color = ErrorRed, fontSize = 11.sp)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    GradientTextField(
                        value         = lastName,
                        onValueChange = { lastName = it },
                        placeholder   = "Last Name",
                        isError       = lastNameError != null,
                        onFocusLost   = { lastNameTouched = true }
                    )
                    if (lastNameError != null) {
                        Spacer(Modifier.height(4.dp))
                        Text(lastNameError, color = ErrorRed, fontSize = 11.sp)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            GradientTextField(
                value         = email,
                onValueChange = { email = it },
                placeholder   = "Email",
                keyboardType  = KeyboardType.Email,
                isError       = emailError != null,
                onFocusLost   = { emailTouched = true }
            )
            if (emailError != null) {
                Spacer(Modifier.height(4.dp))
                Text(emailError, color = ErrorRed, fontSize = 12.sp)
            }

            Spacer(Modifier.height(14.dp))
            GradientTextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = "Password",
                isPassword    = true,
                isError       = passwordError != null,
                onFocusLost   = { passwordTouched = true }
            )
            if (passwordError != null) {
                Spacer(Modifier.height(4.dp))
                Text(passwordError, color = ErrorRed, fontSize = 12.sp)
            }

            Spacer(Modifier.height(14.dp))
            GradientTextField(
                value         = confirmPass,
                onValueChange = { confirmPass = it },
                placeholder   = "Confirm Password",
                isPassword    = true,
                isError       = confirmError != null,
                onFocusLost   = { confirmPassTouched = true }
            )
            if (confirmError != null) {
                Spacer(Modifier.height(4.dp))
                Text(confirmError, color = ErrorRed, fontSize = 12.sp)
            }

            Spacer(Modifier.height(22.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked         = agreed,
                    onCheckedChange = { agreed = it },
                    colors          = CheckboxDefaults.colors(
                        checkedColor   = GreenLight,
                        uncheckedColor = White.copy(alpha = 0.7f)
                    )
                )
                Text(
                    "I accept all the ",
                    color    = White.copy(alpha = 0.75f),
                    fontSize = 12.sp
                )
                Text(
                    "Terms & Condition",
                    color      = White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier   = Modifier.clickable { }
                )
            }
            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(10.dp))
                Text(uiState.errorMessage ?: "", color = Color(0xFFFFCDD2), fontSize = 13.sp)
            }

            Spacer(Modifier.height(28.dp))

            HomieeButton(
                text    = if (uiState.isLoading) "Signing Up..." else "Sign Up",
                enabled = agreed && isFormValid && !uiState.isLoading,
                onClick = {
                    firstNameTouched   = true
                    lastNameTouched    = true
                    emailTouched       = true
                    passwordTouched    = true
                    confirmPassTouched = true
                    viewModel?.register(
                        firstName = firstName,
                        lastName  = lastName,
                        email     = email,
                        password  = password,
                        password2 = confirmPass
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Already a HoMiee Helper? ",
                    color = White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Text(
                    "Login here",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate(Routes.LOGIN_ROUTE) }
                )
            }


            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = White.copy(alpha = 0.4f))
                Text("  OR  ", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                Divider(modifier = Modifier.weight(1f), color = White.copy(alpha = 0.4f))
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick  = { /* TODO: Google sign-in */ },
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(containerColor = White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    painter            = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    tint               = Color.Unspecified,
                    modifier           = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Sign In with Google", color = TextPrimary, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(32.dp))

            Spacer(Modifier.height(18.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = White.copy(alpha = 0.8f), fontSize = 13.sp)
                Text(
                    "Sign In",
                    color      = White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp,
                    modifier   = Modifier.clickable { navController.navigate(Routes.LOGIN_ROUTE) }
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
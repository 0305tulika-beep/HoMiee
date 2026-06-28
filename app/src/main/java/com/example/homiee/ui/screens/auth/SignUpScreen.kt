package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.GradientTextField
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.White
import com.example.homiee.viewmodel.RegisterViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel(),
    onSignedUp: (String) -> Unit = {}
)  {
    var firstName   by remember { mutableStateOf("") }
    var lastName    by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var mobile      by remember { mutableStateOf("") }
    var username    by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var agreed      by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(Routes.OTP_ROUTE)
            viewModel.resetState()
        }
    }

    HideSystemBars(lightIcons = true)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter            = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(systemBarsPadding())
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Account", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = White)
            Text("Join HoMiee", color = White.copy(alpha = 0.85f), fontSize = 15.sp)

            Spacer(Modifier.height(28.dp))

            GradientTextField(firstName,   { firstName   = it }, "First Name")
            Spacer(Modifier.height(12.dp))
            GradientTextField(lastName,    { lastName    = it }, "Last Name")
            Spacer(Modifier.height(12.dp))
            GradientTextField(
                value         = email,
                onValueChange = { email = it },
                placeholder   = "Email",
                keyboardType  = KeyboardType.Email
            )
            Spacer(Modifier.height(12.dp))
            GradientTextField(
                value         = mobile,
                onValueChange = { mobile = it },
                placeholder   = "Mobile Number",
                keyboardType  = KeyboardType.Phone
            )
            Spacer(Modifier.height(12.dp))
            GradientTextField(username,    { username    = it }, "Username")
            Spacer(Modifier.height(12.dp))
            GradientTextField(password,    { password    = it }, "Password",         isPassword = true)
            Spacer(Modifier.height(12.dp))
            GradientTextField(confirmPass, { confirmPass = it }, "Confirm Password", isPassword = true)

            Spacer(Modifier.height(16.dp))

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
                    "By signing up you agree to HoMiee's ",
                    color    = White.copy(alpha = 0.75f),
                    fontSize = 12.sp
                )
            }
            Row(Modifier.padding(start = 48.dp)) {
                Text(
                    "Terms of Service",
                    color      = White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 12.sp,
                    modifier   = Modifier.clickable { }
                )
                Text(" and ", color = White.copy(alpha = 0.75f), fontSize = 12.sp)
                Text(
                    "Privacy Policy",
                    color      = White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 12.sp,
                    modifier   = Modifier.clickable { }
                )
            }

            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(uiState.errorMessage ?: "", color = Color(0xFFFFCDD2), fontSize = 13.sp)
            }

            Spacer(Modifier.height(20.dp))

            HomieeButton(
                text    = if (uiState.isLoading) "Signing Up..." else "Sign Up",
                enabled = agreed && !uiState.isLoading,
                onClick = {
                    viewModel.register(
                        firstName = firstName,
                        lastName  = lastName,
                        email     = email,
                        mobile    = mobile,
                        username  = username,
                        password  = password,
                        password2 = confirmPass
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

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
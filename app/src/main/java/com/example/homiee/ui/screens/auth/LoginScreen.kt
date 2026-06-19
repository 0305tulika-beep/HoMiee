package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.GradientTextField
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.gradientBackground
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.homiee.R
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    role: String,                              // ← NEW: "resident" or "helper", passed from Navgraph
    viewModel: LoginViewModel = viewModel()
) {
    var username   by remember { mutableStateOf("") }
    var password   by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Login success → navigate based on which flow (resident/helper) we're already in
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            val destination = if (role == "helper") Routes.HOME_HELP else Routes.HOME_RES
            navController.navigate(destination) {
                popUpTo(Routes.CHOICE) { inclusive = false }
            }
            viewModel.resetState()
        }
    }

    HideSystemBars()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .padding(systemBarsPadding())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text       = "Welcome\nBack!",
                fontSize   = 38.sp,
                fontWeight = FontWeight.Bold,
                color      = White,
                lineHeight = 46.sp
            )

            Spacer(Modifier.height(32.dp))

            GradientTextField(
                value         = username,
                onValueChange = { username = it },
                placeholder   = "Username, Email or Mobile"
            )

            Spacer(Modifier.height(12.dp))

            GradientTextField(
                value         = password,
                onValueChange = { password = it },
                placeholder   = "Password",
                isPassword    = true
            )

            Spacer(Modifier.height(12.dp))

            // Remember Me + Forgotten password row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked         = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors          = CheckboxDefaults.colors(
                            checkedColor   = GreenLight,
                            uncheckedColor = White.copy(alpha = 0.7f),
                            checkmarkColor = White
                        )
                    )
                    Text("Remember Me", color = White, fontSize = 13.sp)
                }
                Text(
                    text     = "Forgotten password?",
                    color    = White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { /* TODO: forgot password */ }
                )
            }

            // Show error message if login failed
            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text     = uiState.errorMessage ?: "",
                    color    = Color(0xFFFFCDD2),
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            HomieeButton(
                text    = if (uiState.isLoading) "Logging In..." else "Log In",
                enabled = !uiState.isLoading,
                onClick = {
                    viewModel.login(identifier = username, password = password)
                }
            )

            Spacer(Modifier.height(20.dp))

            // OR divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = White.copy(alpha = 0.4f))
                Text("  OR  ", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                Divider(modifier = Modifier.weight(1f), color = White.copy(alpha = 0.4f))
            }

            Spacer(Modifier.height(20.dp))

            // Google Sign-In button
            OutlinedButton(
                onClick  = { /* TODO: Google sign-in */ },
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(containerColor = White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text("Sign In with Google", color = TextPrimary, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(32.dp))

            // Sign Up link
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Don't have an account yet? ",
                    color    = White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
                Text(
                    text       = "Sign Up",
                    color      = White,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier
                        .clickable { navController.navigate(Routes.CHOICE) }
                        .padding(start = 2.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
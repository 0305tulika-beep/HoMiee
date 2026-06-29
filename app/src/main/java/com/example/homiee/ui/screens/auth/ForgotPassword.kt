package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.homiee.R
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar

private val GreenPrimary = Color(0xFF1A5C3A)
private val GreenDark    = Color(0xFF0F3D26)
private val TextHint     = Color(0xFF9E9E9E)
private val FieldBg      = Color(0xFFF5F5F5)

@Composable
fun ForgotPasswordScreen(
    onBack:     () -> Unit = {},
    onContinue: (String) -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    var email by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // Full-screen bg3 background
        Image(
            painter            = painterResource(R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.FillBounds,
            modifier           = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {

            // ── Back arrow ────────────────────────────────────────────────
            IconButton(
                onClick  = onBack,
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .size(44.dp)
            ) {
                Icon(
                    painter            = painterResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    tint               = Color.White,
                    modifier           = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Title block ───────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 28.dp)) {
                Text(
                    text       = "Find Your Account",
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text     = "Enter your email",
                    fontSize = 15.sp,
                    color    = Color.White.copy(alpha = 0.85f)
                )
            }

            Spacer(Modifier.height(36.dp))

            // ── Card ──────────────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    // Email field
                    Text(
                        text       = "Email",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF333333)
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        placeholder   = {
                            Text("xyz@gmail.com", color = TextHint, fontSize = 14.sp)
                        },
                        singleLine    = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor   = FieldBg,
                            unfocusedContainerColor = FieldBg,
                            focusedBorderColor      = GreenPrimary,
                            unfocusedBorderColor    = Color.Transparent,
                            cursorColor             = GreenPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(24.dp))

                    // Continue button
                    Button(
                        onClick  = { if (email.isNotBlank()) onContinue(email.trim()) },
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = GreenDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(
                            text       = "Continue",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = Color.White
                        )
                    }
                }
            }
        }
    }
}
package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.OtpBox
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.GreenTint
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White
import com.example.homiee.viewmodel.OtpViewModel
import com.example.homiee.viewmodel.OtpViewModelFactory

@Composable
fun OtpScreen(
    email: String,
    onConfirm: () -> Unit,
    viewModel: OtpViewModel = viewModel(
        factory = OtpViewModelFactory(LocalContext.current)
    )
) {
    var otpValue by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val resendState by viewModel.resendState.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onConfirm()
            viewModel.resetState()
        }
    }

    HideSystemBars(lightIcons = false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(systemBarsPadding())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(56.dp))

            // Icon badge
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(GreenTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Filled.MarkEmailRead,
                    contentDescription = null,
                    tint               = GreenDark,
                    modifier           = Modifier.size(34.dp)
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text       = "Verify Your Email",
                fontWeight = FontWeight.Bold,
                fontSize   = 22.sp,
                color      = TextPrimary,
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text      = "We've sent a 6-digit code to",
                fontSize  = 14.sp,
                color     = TextMuted,
                textAlign = TextAlign.Center
            )

            Text(
                text       = email,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary,
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
            ) {
                repeat(6) { index ->
                    OtpBox(digit = otpValue.getOrNull(index)?.toString() ?: "")
                }
            }

            OutlinedTextField(
                value           = otpValue,
                onValueChange   = { if (it.length <= 6) otpValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier        = Modifier
                    .size(1.dp)
                    .focusRequester(focusRequester),
                colors          = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor   = Color.Transparent
                )
            )

            if (uiState.errorMessage != null) {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFDECEC))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text     = uiState.errorMessage ?: "",
                        color    = Color(0xFFD32F2F),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(36.dp))

            HomieeButton(
                text     = if (uiState.isLoading) "Verifying..." else "Confirm",
                enabled  = otpValue.length == 6 && !uiState.isLoading,
                onClick  = {
                    viewModel.verifyOtp(email = email, otp = otpValue)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text     = if (resendState.isLoading) "Resending... " else "Didn't receive a code? ",
                    color    = TextMuted,
                    fontSize = 14.sp
                )
                Text(
                    text       = "Resend",
                    color      = GreenMid,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    modifier   = Modifier.clickable {
                        viewModel.resendOtp(email = email)
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
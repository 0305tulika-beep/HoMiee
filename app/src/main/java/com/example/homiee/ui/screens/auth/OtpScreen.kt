package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.R
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.HomieeButton
import com.example.homiee.ui.components.OtpBox
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenMid
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

    HideSystemBars(lightIcons = true)

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter            = painterResource(id = R.drawable.bg3),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 170.dp)
        ) {
            Image(
                painter            = painterResource(id = R.drawable.bgw),
                contentDescription = null,
                contentScale       = ContentScale.FillBounds,
                modifier           = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(systemBarsPadding())
                    .padding(horizontal = 32.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text       = "Enter Security Pin",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = TextPrimary
                )

                Spacer(Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text     = uiState.errorMessage ?: "",
                        color    = Color(0xFFD32F2F),
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(40.dp))

                HomieeButton(
                    text     = if (uiState.isLoading) "Verifying..." else "Confirm",
                    enabled  = otpValue.length == 6 && !uiState.isLoading,
                    onClick  = {
                        viewModel.verifyOtp(email = email, otp = otpValue)   // ← role removed
                    },
                    modifier = Modifier.fillMaxWidth(0.65f)
                )

                Spacer(Modifier.height(20.dp))

                Row {
                    Text(
                        text = if (resendState.isLoading) "Resending... " else "Didn't receive yet? ",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                    Text(
                        text       = "Resend it",
                        color      = GreenMid,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                        modifier   = Modifier.clickable {
                            viewModel.resendOtp(email = email)
                        }
                    )
                }
            }
        }

        Text(
            text       = "Verify Your Number",
            style      = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color      = White,
            textAlign  = TextAlign.Center,
            modifier   = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 70.dp)
        )
    }
}
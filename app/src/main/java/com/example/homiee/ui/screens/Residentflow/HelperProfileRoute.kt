package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.viewmodel.HelperProfileViewModel

private val RouteGreen      = Color(0xFF1A5C3A)
private val RouteGreenLight = Color(0xFFE8F5EE)
private val RouteError      = Color(0xFFD64545)

/**
 * Loads the helper from the API, then shows [HelperProfileScreen].
 * While loading (or if it fails) it shows a spinner / error with Retry.
 */
@Composable
fun HelperProfileRoute(
    helperId: String,
    viewModel: HelperProfileViewModel,
    onBookNow: (String) -> Unit,
    onBack: () -> Unit,
    onChat: () -> Unit,
    onViewReviews: (String) -> Unit
) {
    val helper = viewModel.helper

    if (helper != null) {
        HelperProfileScreen(
            helperId = helperId,
            helper = helper,
            onBookNow = onBookNow,
            onBack = onBack,
            onChat = onChat,
            onViewReviews = onViewReviews
        )
    } else {
        TransparentStatusBarWhiteNavBar(lightStatusBarIcons = false)

        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(RouteGreenLight)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = RouteGreen,
                    modifier           = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = RouteGreen)
                } else {
                    Text(
                        text      = viewModel.errorMessage ?: "Something went wrong. Please try again.",
                        fontSize  = 14.sp,
                        color     = RouteError,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.load() },
                        colors  = ButtonDefaults.buttonColors(containerColor = RouteGreen)
                    ) {
                        Text("Retry", color = Color.White)
                    }
                }
            }
        }
    }
}
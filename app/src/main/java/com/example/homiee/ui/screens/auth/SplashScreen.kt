package com.example.homiee.ui.screens.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.homiee.R
import com.example.homiee.data.local.TokenManager
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.GreenLight
import com.example.homiee.ui.theme.GreenMid
import com.example.homiee.ui.theme.White


@Composable
fun SplashScreen(onFinished: (String) -> Unit) {   // ← CHANGED: now passes the destination route
    val view = LocalView.current
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }   // ← NEW

    // Logo: pops in with overshoot (scale bounces past 1f then settles)
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }

    // Text: slides up from below + fades in, slightly after the logo
    val textOffsetY = remember { Animatable(40f) }
    val textAlpha = remember { Animatable(0f) }

    // Hide system bars while Splash is visible, restore them on exit
    DisposableEffect(Unit) {
        val window = (view.context as android.app.Activity).window
        val controller = WindowInsetsControllerCompat(window, view)
        controller.hide(WindowInsetsCompat.Type.systemBars())


        onDispose {
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    // Logo pop animation — spring gives the "pop" overshoot feel
    LaunchedEffect(Unit) {
        logoAlpha.animateTo(1f, animationSpec = tween(durationMillis = 300))
    }
    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    // Text slides up + fades in, starting ~150ms after the logo begins
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(350)
        textAlpha.animateTo(1f, animationSpec = tween(durationMillis = 300))
    }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(350)
        textOffsetY.animateTo(0f, animationSpec = tween(durationMillis = 350))
    }

    // ── CHANGED: after the delay, check TokenManager and decide where to go ──
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1600)

        val token = tokenManager.getAccessToken()
        val role  = tokenManager.getRole()

        val destination = if (token != null && role != null) {
            if (role == "helper") Routes.HELP_FORM_1 else Routes.RES_FORM_1
        } else {
            Routes.CHOICE
        }

        onFinished(destination)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter            = painterResource(id = R.drawable.logo),
                contentDescription = "HoMiee Logo",
                modifier = Modifier
                    .size(100.dp)
                    .scale(logoScale.value)
                    .graphicsLayer { alpha = logoAlpha.value }
            )
            Spacer(Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    alpha = textAlpha.value
                    translationY = textOffsetY.value
                }
            ) {
                Text(
                    text       = "HoMiee",
                    fontSize   = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF7ED4C9)
                )
            }
        }
    }
}
package com.example.homiee.ui.screens.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.gradientBackground
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.White
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.homiee.R
import com.example.homiee.ui.components.HideSystemBars
import com.example.homiee.ui.components.systemBarsPadding

@Composable
fun ChoiceScreen(navController: NavController) {
    val view = LocalView.current

    // Hide system bars while Choice is visible, restore them on exit
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
                .padding(systemBarsPadding())
                .padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            Text(
                text       = "Welcome !",
                fontSize   = 34.sp,
                fontWeight = FontWeight.Bold,
                color      = White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "How would you like to use HoMiee?",
                style = MaterialTheme.typography.bodyLarge,
                color = White.copy(alpha = 0.9f)
            )

            Spacer(Modifier.height(40.dp))

            RoleCard(
                icon     = R.drawable.img_1,
                title    = "I'm a Resident",
                subtitle = "Find & hire trusted helpers",
                onClick  = { navController.navigate(Routes.RESIDENT_AUTH_GRAPH) }
            )

            Spacer(Modifier.height(20.dp))

            RoleCard(
                icon     = R.drawable.img_2,
                title    = "I'm a Helper",
                subtitle = "Find jobs near you",
                onClick  = { navController.navigate(Routes.HELPER_AUTH_GRAPH) }
            )
        }
    }
}

@Composable
private fun RoleCard(
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, White.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .background(White.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenDark)
            ) {
                Image(
                    painter            = painterResource(id = icon),
                    contentDescription = null,
                    modifier           = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title,    color = White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(subtitle, color = White.copy(alpha = 0.8f), fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = White)
        }
    }
}
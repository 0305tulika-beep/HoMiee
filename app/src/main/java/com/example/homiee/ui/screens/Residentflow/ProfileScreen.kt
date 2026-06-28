package com.example.homiee.ui.screens.Residentflow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.components.systemBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White

@Composable
fun ProfileScreen(
    onNavItemClick:   (String) -> Unit = {},
    onSettingsClick:  () -> Unit = {},
    onMyReviewsClick: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab   = NavTab.ACCOUNT,
                onTabSelected = { tab ->
                    val route = when (tab) {
                        NavTab.HOME     -> Routes.HOME_RES
                        NavTab.SEARCH   -> Routes.SEARCH
                        NavTab.BOOKINGS -> Routes.BOOKINGS
                        NavTab.MESSAGE  -> Routes.MESSAGES
                        NavTab.ACCOUNT  -> Routes.ACCOUNT
                    }
                    onNavItemClick(route)
                }
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter            = painterResource(id = R.drawable.bg),
                contentDescription = null,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                // ── Header ──────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(systemBarsPadding())
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text       = "PROFILE",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = White,
                        modifier   = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text     = "⚙",
                        fontSize = 22.sp,
                        color    = White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable { onSettingsClick() }
                    )
                }

                // ── Scrollable body ──────────────────────────────────────────
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ── Profile card ─────────────────────────────────────────
                    Card(
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(
                            modifier            = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier         = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2E7D67)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter            = painterResource(id = R.drawable.ic_profile_placeholder),
                                    contentDescription = "Profile",
                                    modifier           = Modifier.size(40.dp)
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Text("Priya Sharma",   fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                            Text("+91 9999999999", fontSize = 13.sp, color = TextMuted)
                            Spacer(Modifier.height(12.dp))
                            OutlinedButton(
                                onClick  = {},
                                shape    = RoundedCornerShape(8.dp),
                                border   = BorderStroke(1.dp, GreenDark),
                                colors   = ButtonDefaults.outlinedButtonColors(contentColor = GreenDark)
                            ) {
                                Text("Edit Profile", color = GreenDark, fontSize = 13.sp)
                            }
                        }
                    }

                    // ── Personal Details ─────────────────────────────────────
                    SectionTitle("PERSONAL DETAILS")
                    Card(
                        shape     = RoundedCornerShape(12.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ProfileDetailItem(label = "NAME",    value = "Priya Sharma")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF0F0F0))
                            ProfileDetailItem(label = "PHONE",   value = "+91 9999999999")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF0F0F0))
                            ProfileDetailItem(label = "EMAIL",   value = "blabla@gmail.com")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF0F0F0))
                            ProfileDetailItem(label = "ADDRESS", value = "hehehehehe")
                        }
                    }

                    // ── Trusted Contacts ─────────────────────────────────────
                    SectionTitle("TRUSTED CONTACTS")
                    Card(
                        shape     = RoundedCornerShape(12.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            TrustedContactItem(name = "Sunita Sharma", relation = "Mother", phone = "+91 xxxxxxxxxx")
                            Spacer(Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFFF0F0F0))
                            Spacer(Modifier.height(8.dp))
                            TrustedContactItem(name = "Sunita Sharma", relation = "Mother", phone = "+91 xxxxxxxxxx")
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick  = {},
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                shape    = RoundedCornerShape(20.dp),
                                colors   = ButtonDefaults.buttonColors(containerColor = GreenDark)
                            ) {
                                Text("ADD+", color = White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // ── My Reviews ───────────────────────────────────────────
                    SectionTitle("MY REVIEWS")
                    Card(
                        shape     = RoundedCornerShape(12.dp),
                        colors    = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { onMyReviewsClick() }
                    ) {
                        Row(
                            modifier              = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text("Reviews you've given", fontSize = 14.sp, color = TextPrimary)
                            Text("22", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                        }
                    }
                }
            }
        }
    }
}

// ── Sub-composables ────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(
        text          = text,
        fontSize      = 18.sp,
        fontWeight    = FontWeight.Bold,
        color         = GreenDark,
        letterSpacing = 0.8.sp,
        modifier      = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun ProfileDetailItem(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 11.sp, color = TextMuted, letterSpacing = 0.5.sp)
        Text(value, fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TrustedContactItem(name: String, relation: String, phone: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier         = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(GreenDark),
            contentAlignment = Alignment.Center
        ) {
            Text("RK", color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(name,               fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            Text("$relation $phone", fontSize = 12.sp, color = TextMuted)
        }
    }
}
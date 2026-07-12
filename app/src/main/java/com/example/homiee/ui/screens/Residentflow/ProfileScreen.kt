package com.example.homiee.ui.screens.Residentflow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.R
import com.example.homiee.navigation.Routes
import com.example.homiee.ui.components.BottomNavBar
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.components.TransparentStatusBarWhiteNavBar
import com.example.homiee.ui.components.statusBarsPadding
import com.example.homiee.ui.theme.GreenDark
import com.example.homiee.ui.theme.TextMuted
import com.example.homiee.ui.theme.TextPrimary
import com.example.homiee.ui.theme.White
import com.example.homiee.viewmodel.AccountViewModel
import com.example.homiee.viewmodel.AccountViewModelFactory

@Composable
fun ProfileScreen(
    onNavItemClick:   (String) -> Unit = {},
    onMyReviewsClick: () -> Unit = {},
    onLoggedOut:      () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    val context = LocalContext.current
    val accountViewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(context)
    )

    var showSettings by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deactivatePassword by remember { mutableStateOf("") }
    var deletePassword by remember { mutableStateOf("") }
    var deactivatePasswordVisible by remember { mutableStateOf(false) }
    var deletePasswordVisible by remember { mutableStateOf(false) }

    val logoutState by accountViewModel.logoutState.collectAsState()
    val deactivateState by accountViewModel.deactivateState.collectAsState()
    val deleteState by accountViewModel.deleteState.collectAsState()

    // Navigate away once any of the three actions succeeds
    LaunchedEffect(logoutState.isSuccess, deactivateState.isSuccess, deleteState.isSuccess) {
        if (logoutState.isSuccess || deactivateState.isSuccess || deleteState.isSuccess) {
            onLoggedOut()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            val offsetY by animateDpAsState(
                targetValue = if (showSettings) 100.dp else 0.dp,
                label = "bottomNavOffset"
            )
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
                },
                modifier = Modifier.offset(y = offsetY)
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
                        .padding(statusBarsPadding())
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text       = "PROFILE",
                        fontSize   = 25.sp,
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
                            .clickable { showSettings = true }
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
                            ProfileDetailItem(label = "EMAIL",   value = "blabla@gmail.com")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF0F0F0))
                            ProfileDetailItem(label = "ADDRESS", value = "hehehehehe")
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

            // ── Settings slide-in panel (overlays everything, incl. bottom bar) ──
            Box(modifier = Modifier.fillMaxSize()) {

                // Scrim
                AnimatedVisibility(
                    visible = showSettings,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { showSettings = false }
                    )
                }

                // Panel
                AnimatedVisibility(
                    visible = showSettings,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it }),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.78f),
                        color = White,
                        shadowElevation = 12.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(statusBarsPadding())
                                .padding(horizontal = 20.dp)
                        ) {
                            Spacer(Modifier.height(24.dp))

                            Text(
                                text = "Settings",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                            Spacer(Modifier.height(32.dp))

                            SettingsOptionButton(
                                label = "Logout",
                                onClick = {
                                    showSettings = false
                                    showLogoutDialog = true
                                }
                            )
                            Spacer(Modifier.height(14.dp))
                            SettingsOptionButton(
                                label = "Deactivate Account",
                                onClick = {
                                    showSettings = false
                                    showDeactivateDialog = true
                                }
                            )
                            Spacer(Modifier.height(14.dp))
                            SettingsOptionButton(
                                label = "Delete Account",
                                onClick = {
                                    showSettings = false
                                    showDeleteDialog = true
                                },
                                isDestructive = true
                            )
                        }
                    }
                }
            }
        }
    }

    // ── Logout confirmation ─────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!logoutState.isLoading) {
                    showLogoutDialog = false
                    accountViewModel.resetLogoutState()
                }
            },
            title = { Text("Logout?") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = { accountViewModel.logout() },
                    enabled = !logoutState.isLoading
                ) {
                    Text(
                        if (logoutState.isLoading) "Logging out..." else "Logout",
                        color = Color(0xFFD32F2F)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        accountViewModel.resetLogoutState()
                    },
                    enabled = !logoutState.isLoading
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // ── Deactivate confirmation (requires password) ─────────────────────────
    if (showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!deactivateState.isLoading) {
                    showDeactivateDialog = false
                    deactivatePassword = ""
                    deactivatePasswordVisible = false
                    accountViewModel.resetDeactivateState()
                }
            },
            title = { Text("Deactivate account?") },
            text = {
                Column {
                    Text("Your account will be set as inactive and you'll be logged out on all devices. Enter your password to confirm.")
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = deactivatePassword,
                        onValueChange = { deactivatePassword = it },
                        label = { Text("Password") },
                        visualTransformation = if (deactivatePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { deactivatePasswordVisible = !deactivatePasswordVisible }) {
                                Icon(
                                    imageVector = if (deactivatePasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (deactivatePasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        singleLine = true,
                        isError = deactivateState.errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    deactivateState.errorMessage?.let {
                        Text(
                            it,
                            color = Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { accountViewModel.deactivateAccount(deactivatePassword) },
                    enabled = !deactivateState.isLoading
                ) {
                    Text(
                        if (deactivateState.isLoading) "Please wait..." else "Deactivate",
                        color = Color(0xFFD32F2F)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeactivateDialog = false
                        deactivatePassword = ""
                        deactivatePasswordVisible = false
                        accountViewModel.resetDeactivateState()
                    },
                    enabled = !deactivateState.isLoading
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // ── Delete confirmation (requires password) ──────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!deleteState.isLoading) {
                    showDeleteDialog = false
                    deletePassword = ""
                    deletePasswordVisible = false
                    accountViewModel.resetDeleteState()
                }
            },
            title = { Text("Delete account permanently?") },
            text = {
                Column {
                    Text("This cannot be undone. All your data will be permanently removed. Enter your password to confirm.")
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = deletePassword,
                        onValueChange = { deletePassword = it },
                        label = { Text("Password") },
                        visualTransformation = if (deletePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { deletePasswordVisible = !deletePasswordVisible }) {
                                Icon(
                                    imageVector = if (deletePasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (deletePasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        singleLine = true,
                        isError = deleteState.errorMessage != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    deleteState.errorMessage?.let {
                        Text(
                            it,
                            color = Color(0xFFD32F2F),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { accountViewModel.deleteAccount(deletePassword) },
                    enabled = !deleteState.isLoading
                ) {
                    Text(
                        if (deleteState.isLoading) "Deleting..." else "Delete",
                        color = Color(0xFFD32F2F)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        deletePassword = ""
                        deletePasswordVisible = false
                        accountViewModel.resetDeleteState()
                    },
                    enabled = !deleteState.isLoading
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text          = text,
        fontSize      = 18.sp,
        fontWeight    = FontWeight.Bold,
        color         = TextPrimary,
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
private fun SettingsOptionButton(
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDestructive) Color(0xFFD32F2F) else Color(0xFF2E7D67)
        )
    ) {
        Text(label, color = White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
    }
}
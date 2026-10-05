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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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
import com.example.homiee.viewmodel.ProfileViewModel
import com.example.homiee.viewmodel.ProfileViewModelFactory

// ── Colors ────────────────────────────────────────────────────────────────
private val AvatarGreen  = Color(0xFF2E7D67)
private val ErrorRed     = Color(0xFFD32F2F)
private val DividerGray  = Color(0xFFF0F0F0)
private val ScrimColor   = Color(0x73000000)   // black @ ~45%
private val SettingsMint = Color(0xFFEEF6F5)
private val WarnAmber    = Color(0xFFE5A52B)
private val SubtitleGray = Color(0xFF6B7570)
private val CheckGreen   = Color(0xFF2E9E6B)

@Composable
fun ProfileScreen(
    onNavItemClick:   (String) -> Unit = {},
    onMyReviewsClick: () -> Unit = {},
    onLoggedOut:      () -> Unit = {},
    onContactSupport: () -> Unit = {}
) {
    TransparentStatusBarWhiteNavBar(lightStatusBarIcons = true)

    val context = LocalContext.current
    val accountViewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(context)
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(context)
    )

    val profileState by profileViewModel.uiState.collectAsState()

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
                painter            = painterResource(R.drawable.bg1),
                contentDescription = null,
                contentScale       = ContentScale.FillBounds,
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
                        text       = "Profile",
                        fontSize   = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color      = White,
                        modifier   = Modifier.align(Alignment.CenterStart)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.setting),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(35.dp)
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

                    // ── Load status (spinner / error + retry) ────────────────
                    if (profileState.isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            color = White,
                            trackColor = GreenDark
                        )
                    }
                    profileState.errorMessage?.let { message ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = message,
                                color = White,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { profileViewModel.load() }) {
                                Text("Retry", color = White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // ── Profile card ─────────────────────────────────────────
                    Card(
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(containerColor = White),
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
                                    .background(AvatarGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                if (profileState.photoUrl != null) {
                                    AsyncImage(
                                        model              = profileState.photoUrl,
                                        contentDescription = "Profile",
                                        contentScale       = ContentScale.Crop,
                                        placeholder        = painterResource(id = R.drawable.ic_profile_placeholder),
                                        error              = painterResource(id = R.drawable.ic_profile_placeholder),
                                        modifier           = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Image(
                                        painter            = painterResource(id = R.drawable.ic_profile_placeholder),
                                        contentDescription = "Profile",
                                        modifier           = Modifier.size(40.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                profileState.name.ifBlank { "—" },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
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
                        colors    = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ProfileDetailItem(label = "NAME",    value = profileState.name.ifBlank { "—" })
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = DividerGray)
                            ProfileDetailItem(label = "EMAIL",   value = profileState.email.ifBlank { "—" })
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = DividerGray)
                            ProfileDetailItem(label = "ADDRESS", value = profileState.address.ifBlank { "—" })
                        }
                    }

                    // ── My Reviews ───────────────────────────────────────────
                    SectionTitle("MY REVIEWS")
                    Card(
                        shape     = RoundedCornerShape(12.dp),
                        colors    = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { onMyReviewsClick() }
                    ) {
                        Row(
                            modifier              = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(
                                "Reviews you've given",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "22",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    imageVector        = Icons.Default.ChevronRight,
                                    contentDescription = "Go to reviews",
                                    tint               = TextPrimary,
                                    modifier           = Modifier.size(20.dp)
                                )
                            }
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
                            .background(ScrimColor)
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
                            .fillMaxWidth(0.75f),
                        color = White,
                        shadowElevation = 12.dp
                    ) {
                        SettingsPanel(
                            onClose          = { showSettings = false },
                            onLogout         = { showSettings = false; showLogoutDialog = true },
                            onDeactivate     = { showSettings = false; showDeactivateDialog = true },
                            onDelete         = { showSettings = false; showDeleteDialog = true },
                            onContactSupport = onContactSupport
                        )
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
                        color = ErrorRed
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
                            color = ErrorRed,
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
                        color = ErrorRed
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
                            color = ErrorRed,
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
                        color = ErrorRed
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

// ── Settings panel ──────────────────────────────────────────────────────────
@Composable
private fun SettingsPanel(
    onClose: () -> Unit,
    onLogout: () -> Unit,
    onDeactivate: () -> Unit,
    onDelete: () -> Unit,
    onContactSupport: () -> Unit
) {
    val context = LocalContext.current
    val versionName = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull().orEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(statusBarsPadding())
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // ── Title + close ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close settings",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Header card ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SettingsMint)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GreenDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Manage your account",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Text("and app preferences", color = SubtitleGray, fontSize = 12.sp)
        }

        Spacer(Modifier.height(16.dp))

        // ── Actions ──
        SettingsActionRow(
            icon = Icons.AutoMirrored.Filled.Logout,
            tint = GreenDark,
            title = "Logout",
            subtitle = "Sign out from your account",
            onClick = onLogout
        )
        SettingsActionRow(
            icon = Icons.Default.PauseCircle,
            tint = WarnAmber,
            title = "Deactivate Account",
            subtitle = "Temporarily deactivate your account",
            onClick = onDeactivate
        )
        SettingsActionRow(
            icon = Icons.Default.Delete,
            tint = ErrorRed,
            title = "Delete Account",
            subtitle = "Permanently delete your account and all data",
            onClick = onDelete
        )

        Spacer(Modifier.height(16.dp))

        // ── Our Commitment ──
        Text("Our Commitment", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        CommitmentRow("Your data is safe with us.")
        Spacer(Modifier.height(6.dp))
        CommitmentRow("We respect your privacy.")

        Spacer(Modifier.height(16.dp))

        // ── Need Help? ──
        Text("Need Help?", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        SettingsActionRow(
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            tint = GreenDark,
            title = "Contact Support",
            subtitle = "We're here to help you",
            onClick = onContactSupport
        )

        Spacer(Modifier.height(12.dp))

        // ── App version ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("App Version", color = SubtitleGray, fontSize = 13.sp)
            Text(
                text = if (versionName.isNotBlank()) "v$versionName" else "",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    tint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = tint, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(subtitle, color = SubtitleGray, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun CommitmentRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = CheckGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = SubtitleGray, fontSize = 13.sp)
    }
}

// ── Shared small composables ────────────────────────────────────────────────
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
        verticalAlignment     = Alignment.Top
    ) {
        Text(
            text          = label,
            fontSize      = 11.sp,
            color         = TextMuted,
            letterSpacing = 0.5.sp,
            modifier      = Modifier.padding(top = 2.dp)
        )
        Spacer(Modifier.width(24.dp))
        Text(
            text       = value,
            fontSize   = 13.sp,
            color      = TextPrimary,
            fontWeight = FontWeight.Medium,
            textAlign  = TextAlign.End,
            modifier   = Modifier.weight(1f)
        )
    }
}
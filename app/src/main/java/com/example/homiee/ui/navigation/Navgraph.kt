package com.example.homiee.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.homiee.ui.screens.auth.*
import com.example.homiee.ui.screens.resident.*
import com.example.homiee.ui.screens.helper.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homiee.viewmodel.RegisterViewModel

// ── Route Constants ───────────────────────────────────────
object Routes {
    const val SPLASH         = "splash"
    const val CHOICE         = "choice"

    // Resident
    const val RESIDENT_AUTH_GRAPH = "resident_auth_graph"
    const val SIGNUP_RES     = "signup_resident"
    const val LOGIN_RES      = "login_resident"
    const val OTP_RES        = "otp_resident"
    const val RES_FORM_1     = "res_form_address"
    const val RES_FORM_2     = "res_form_services"
    const val RES_FORM_3     = "res_form_schedule"
    const val RES_FORM_4     = "res_form_identity"
    const val RES_FORM_5     = "res_form_photo"
    const val HOME_RES       = "home_resident"

    // Helper
    const val HELPER_AUTH_GRAPH = "helper_auth_graph"
    const val SIGNUP_HELP    = "signup_helper"
    const val LOGIN_HELP     = "login_helper"
    const val OTP_HELP       = "otp_helper"
    const val HELP_FORM_1    = "help_form_identity"
    const val HELP_FORM_2    = "help_form_services"
    const val HELP_FORM_3    = "help_form_skills"
    const val HELP_FORM_4    = "help_form_availability"
    const val HOME_HELP      = "home_helper"          // placeholder
}

// ── Nav Graph ─────────────────────────────────────────────
@Composable
fun HomieeNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController      = navController,
        startDestination   = Routes.SPLASH,
        enterTransition    = { EnterTransition.None },
        exitTransition     = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {
        // ── Splash ────────────────────────────────────────────────────
        composable(Routes.SPLASH) {
            SplashScreen { destination ->
                navController.navigate(destination) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        // ── Choice (Resident / Helper) ────────────────────────────────
        composable(Routes.CHOICE) {
            ChoiceScreen(navController)
        }

        // ═══════════════════════════════════════════════════════════════
        // RESIDENT FLOW
        // ═══════════════════════════════════════════════════════════════

        navigation(
            startDestination = Routes.SIGNUP_RES,
            route             = Routes.RESIDENT_AUTH_GRAPH
        ) {
            composable(Routes.SIGNUP_RES) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Routes.RESIDENT_AUTH_GRAPH)
                }
                val registerViewModel: RegisterViewModel = viewModel(parentEntry)

                SignUpResidentScreen(navController, registerViewModel)
            }

            composable(Routes.OTP_RES) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Routes.RESIDENT_AUTH_GRAPH)
                }
                val registerViewModel: RegisterViewModel = viewModel(parentEntry)

                OtpScreen(
                    email     = registerViewModel.registeredEmail,
                    role      = "resident",
                    onConfirm = {
                        navController.navigate(Routes.RES_FORM_1) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ── Resident Login ──
        composable(Routes.LOGIN_RES) {
            LoginScreen(navController, role = "resident")
        }

        composable(Routes.RES_FORM_1) {
            ResFormAddressScreen { navController.navigate(Routes.RES_FORM_2) }
        }
        composable(Routes.RES_FORM_2) {
            ResFormServicesScreen { navController.navigate(Routes.RES_FORM_3) }
        }
        composable(Routes.RES_FORM_3) {
            ResFormScheduleScreen { navController.navigate(Routes.RES_FORM_4) }
        }
        composable(Routes.RES_FORM_4) {
            ResFormIdentityScreen { navController.navigate(Routes.RES_FORM_5) }
        }
        composable(Routes.RES_FORM_5) {
            ResFormPhotoScreen {
                navController.navigate(Routes.HOME_RES) {
                    popUpTo(Routes.CHOICE) { inclusive = false }
                }
            }
        }

        composable(Routes.HOME_RES) {
            // TODO: ResidentHomeScreen()
        }

        // ═══════════════════════════════════════════════════════════════
        // HELPER FLOW
        // ═══════════════════════════════════════════════════════════════

        navigation(
            startDestination = Routes.SIGNUP_HELP,
            route             = Routes.HELPER_AUTH_GRAPH
        ) {
            composable(Routes.SIGNUP_HELP) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Routes.HELPER_AUTH_GRAPH)
                }
                val registerViewModel: RegisterViewModel = viewModel(parentEntry)

                SignUpHelperScreen(navController, registerViewModel)
            }

            composable(Routes.OTP_HELP) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry(Routes.HELPER_AUTH_GRAPH)
                }
                val registerViewModel: RegisterViewModel = viewModel(parentEntry)

                OtpScreen(
                    email     = registerViewModel.registeredEmail,
                    role      = "helper",
                    onConfirm = {
                        navController.navigate(Routes.HELP_FORM_1) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ── Helper Login ──
        composable(Routes.LOGIN_HELP) {
            LoginScreen(navController, role = "helper")
        }

        composable(Routes.HELP_FORM_1) {
            HelpFormIdentityScreen { navController.navigate(Routes.HELP_FORM_2) }
        }
        composable(Routes.HELP_FORM_2) {
            HelpFormServicesScreen { navController.navigate(Routes.HELP_FORM_3) }
        }
        composable(Routes.HELP_FORM_3) {
            HelpFormSkillsScreen { navController.navigate(Routes.HELP_FORM_4) }
        }
        composable(Routes.HELP_FORM_4) {
            HelpFormAvailabilityScreen {
                navController.navigate(Routes.HOME_HELP) {
                    popUpTo(Routes.CHOICE) { inclusive = false }
                }
            }
        }

        composable(Routes.HOME_HELP) {
            // TODO: HelperHomeScreen()
        }
    }
}
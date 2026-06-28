package com.example.homiee.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.screens.Residentflow.BookingsScreen
import com.example.homiee.ui.screens.Residentflow.MyReviewsScreen
import com.example.homiee.ui.screens.Residentflow.ProfileScreen
import com.example.homiee.ui.screens.Residentflow.ResidentHomeScreen
import com.example.homiee.ui.screens.Residentflow.SearchScreen
import com.example.homiee.ui.screens.Residentflow.SettingsScreen
import com.example.homiee.ui.screens.auth.*
import com.example.homiee.ui.screens.resident.*
import com.example.homiee.viewmodel.RegisterViewModel
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val SPLASH       = "splash"
    const val SIGNUP_ROUTE = "signup"
    const val LOGIN_ROUTE  = "login"
    const val OTP_ROUTE    = "otp/{email}"

    // Resident forms
    const val RES_FORM_1 = "res_form_address"
    const val RES_FORM_2 = "res_form_services"
    const val RES_FORM_3 = "res_form_schedule"
    const val RES_FORM_4 = "res_form_identity"
    const val RES_FORM_5 = "res_form_photo"

    // Main resident screens
    const val HOME_RES   = "home_resident"
    const val SEARCH     = "search"
    const val BOOKINGS   = "bookings"
    const val MESSAGES   = "messages"
    const val ACCOUNT    = "account"

    // Sub screens (no bottom nav, back returns to profile)
    const val SETTINGS   = "settings"
    const val MY_REVIEWS = "my_reviews"

    fun otpRoute(email: String): String {
        val encoded = URLEncoder.encode(email, "UTF-8")
        return "otp/$encoded"
    }
}

fun NavTab.toRoute() = when (this) {
    NavTab.HOME     -> Routes.HOME_RES
    NavTab.SEARCH   -> Routes.SEARCH
    NavTab.BOOKINGS -> Routes.BOOKINGS
    NavTab.ACCOUNT  -> Routes.ACCOUNT
    NavTab.MESSAGE -> TODO()
}

private fun NavHostController.navigateMain(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME_RES) { saveState = true }
        launchSingleTop = true
        restoreState    = true
    }
}

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

        // ── Splash ──
        composable(Routes.SPLASH) {
            SplashScreen { destination ->
                navController.navigate(destination) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        // ── Signup ──
        composable(Routes.SIGNUP_ROUTE) {
            val registerViewModel: RegisterViewModel = viewModel()
            SignUpScreen(
                navController = navController,
                viewModel     = registerViewModel,
                onSignedUp    = { email ->
                    navController.navigate(Routes.otpRoute(email))
                }
            )
        }

        // ── OTP ──
        composable(
            route     = Routes.OTP_ROUTE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedEmail = backStackEntry.arguments?.getString("email") ?: ""
            val email        = URLDecoder.decode(encodedEmail, "UTF-8")
            OtpScreen(
                email     = email,
                onConfirm = {
                    navController.navigate(Routes.RES_FORM_1) {
                        popUpTo(Routes.SIGNUP_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // ── Login ──
        composable(Routes.LOGIN_ROUTE) {
            LoginScreen(
                navController  = navController,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME_RES) {
                        popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // ── Resident Forms ──
        composable(Routes.RES_FORM_1) { ResFormAddressScreen  { navController.navigate(Routes.RES_FORM_2) } }
        composable(Routes.RES_FORM_2) { ResFormServicesScreen { navController.navigate(Routes.RES_FORM_3) } }
        composable(Routes.RES_FORM_3) { ResFormScheduleScreen { navController.navigate(Routes.RES_FORM_4) } }
        composable(Routes.RES_FORM_4) { ResFormIdentityScreen { navController.navigate(Routes.RES_FORM_5) } }
        composable(Routes.RES_FORM_5) {
            ResFormPhotoScreen {
                navController.navigate(Routes.HOME_RES) {
                    popUpTo(Routes.RES_FORM_1) { inclusive = true }
                }
            }
        }

        // ── Resident Home ── (FLOOR)
        composable(Routes.HOME_RES) {
            ResidentHomeScreen(
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Search ──
        composable(Routes.SEARCH) {
            SearchScreen(
                onViewProfile  = { },
                onBook         = { },
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Bookings ──
        composable(Routes.BOOKINGS) {
            BookingsScreen(
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Messages placeholder ──
        composable(Routes.MESSAGES) { }

        // ── Account / Profile ── (bottom nav screen)
        composable(Routes.ACCOUNT) {
            ProfileScreen(
                onNavItemClick   = { navController.navigateMain(it) },
                onSettingsClick  = { navController.navigate(Routes.SETTINGS) },
                onMyReviewsClick = { navController.navigate(Routes.MY_REVIEWS) }
            )
        }

        // ── Settings ── (no bottom nav, back → profile)
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ── My Reviews ── (no bottom nav, back → profile)
        composable(Routes.MY_REVIEWS) {
            MyReviewsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
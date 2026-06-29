package com.example.homiee.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.screens.Residentflow.*
import com.example.homiee.ui.screens.auth.*
import com.example.homiee.ui.screens.resident.*
import com.example.homiee.viewmodel.BookingViewModel
import com.example.homiee.viewmodel.BookingViewModelFactory
import com.example.homiee.viewmodel.RegisterViewModel
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val SPLASH             = "splash"
    const val SIGNUP_ROUTE       = "signup"
    const val LOGIN_ROUTE        = "login"
    const val OTP_ROUTE          = "otp/{email}/{flow}"   // flow = "login" | "signup"
    const val FORGOT_PASSWORD    = "forgot_password"
    const val TERMS_AND_CONDITIONS = "terms_and_conditions"

    // Resident forms — 4 steps
    const val RES_FORM_1 = "res_form_address"
    const val RES_FORM_2 = "res_form_emergency"
    const val RES_FORM_3 = "res_form_identity"
    const val RES_FORM_4 = "res_form_photo"

    // Main tabs
    const val HOME_RES        = "home_resident"
    const val SEARCH          = "search"
    const val SEARCH_FILTERED = "search/{category}"
    const val BOOKINGS        = "bookings"
    const val MESSAGES        = "messages"
    const val ACCOUNT         = "account"

    // Sub-screens
    const val SETTINGS       = "settings"
    const val MY_REVIEWS     = "my_reviews"
    const val HELPER_PROFILE = "helper_profile/{helperId}"

    // Booking flow
    const val NEW_BOOKING       = "new_booking/{helperName}/{service}/{rating}"
    const val BOOKING_CONFIRMED = "booking_confirmed/{bookingId}"
    const val BOOKING_DETAILS   = "booking_details/{bookingId}"

    // Chat / Activity / Feedback
    const val CHAT     = "chat/{threadId}/{helperName}/{service}"
    const val ACTIVITY = "activity/{bookingId}/{helperName}"
    const val FEEDBACK = "feedback/{bookingId}/{helperName}"

    fun otpRoute(email: String, flow: String): String {
        val encoded = URLEncoder.encode(email, "UTF-8")
        return "otp/$encoded/$flow"
    }
    fun searchFilteredRoute(category: String): String {
        val encoded = URLEncoder.encode(category, "UTF-8")
        return "search/$encoded"
    }
    fun activityRoute(bookingId: String, helperName: String): String {
        val encodedName = URLEncoder.encode(helperName, "UTF-8")
        return "activity/$bookingId/$encodedName"
    }
    fun feedbackRoute(bookingId: String, helperName: String): String {
        val encodedName = URLEncoder.encode(helperName, "UTF-8")
        return "feedback/$bookingId/$encodedName"
    }
    fun helperProfileRoute(helperId: String)     = "helper_profile/$helperId"
    fun bookingConfirmedRoute(bookingId: String) = "booking_confirmed/$bookingId"
    fun bookingDetailsRoute(bookingId: String)   = "booking_details/$bookingId"
    fun chatRoute(threadId: String, helperName: String, service: String): String {
        val encodedName    = URLEncoder.encode(helperName, "UTF-8")
        val encodedService = URLEncoder.encode(service,    "UTF-8")
        return "chat/$threadId/$encodedName/$encodedService"
    }
    fun newBookingRoute(helperName: String, service: String, rating: Float): String {
        val encodedName    = URLEncoder.encode(helperName, "UTF-8")
        val encodedService = URLEncoder.encode(service,    "UTF-8")
        return "new_booking/$encodedName/$encodedService/$rating"
    }
}

fun NavTab.toRoute() = when (this) {
    NavTab.HOME     -> Routes.HOME_RES
    NavTab.SEARCH   -> Routes.SEARCH
    NavTab.BOOKINGS -> Routes.BOOKINGS
    NavTab.MESSAGE  -> Routes.MESSAGES
    NavTab.ACCOUNT  -> Routes.ACCOUNT
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

    val context = LocalContext.current
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(context)
    )

    NavHost(
        navController      = navController,
        startDestination   = Routes.SPLASH,
        enterTransition    = { EnterTransition.None },
        exitTransition     = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {

        // ── Splash ──────────────────────────────────────────────────────────
        composable(Routes.SPLASH) {
            SplashScreen { destination ->
                navController.navigate(destination) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        // ── Login ────────────────────────────────────────────────────────────
        // Back → close app
        composable(Routes.LOGIN_ROUTE) {
            BackHandler { (context as? Activity)?.finish() }
            LoginScreen(
                navController    = navController,
                onLoginSuccess   = { email ->
                    navController.navigate(Routes.otpRoute(email, "login")) {
                        popUpTo(Routes.LOGIN_ROUTE) { inclusive = false }
                    }
                },
                onForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }

        // ── Forgot Password ───────────────────────────────────────────────────
        // Back → Login (normal popBackStack)
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack     = { navController.popBackStack() },
                onContinue = { _ ->
                    // TODO: call reset-password API; then navigate accordingly
                    navController.popBackStack()
                }
            )
        }

        // ── Signup ──────────────────────────────────────────────────────────
        composable(Routes.SIGNUP_ROUTE) {
            val registerViewModel: RegisterViewModel = viewModel()
            SignUpScreen(
                navController = navController,
                viewModel     = registerViewModel,
                onSignedUp    = { email ->
                    navController.navigate(Routes.otpRoute(email, "signup")) {
                        popUpTo(Routes.SIGNUP_ROUTE) { inclusive = false }
                    }
                }
            )
        }

        // ── OTP ─────────────────────────────────────────────────────────────
        // login  → HOME  (clear all auth)
        // signup → TERMS (clear otp from stack)
        composable(
            route     = Routes.OTP_ROUTE,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("flow")  { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "", "UTF-8"
            )
            val flow = backStackEntry.arguments?.getString("flow") ?: "login"

            OtpScreen(
                email     = email,
                onConfirm = {
                    if (flow == "login") {
                        navController.navigate(Routes.HOME_RES) {
                            popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.TERMS_AND_CONDITIONS) {
                            popUpTo(Routes.OTP_ROUTE) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ── Terms & Conditions ────────────────────────────────────────────────
        // Back → close app; Agree → RES_FORM_1 (clear all signup screens)
        composable(Routes.TERMS_AND_CONDITIONS) {
            TermsAndConditionsScreen(
                onAgree = {
                    navController.navigate(Routes.RES_FORM_1) {
                        popUpTo(Routes.SIGNUP_ROUTE) { inclusive = true }
                    }
                },
                onClose = { (context as? Activity)?.finish() }
            )
        }

        // ── Resident Forms ───────────────────────────────────────────────────
        // Back from any form step → close app
        composable(Routes.RES_FORM_1) {
            BackHandler { (context as? Activity)?.finish() }
            ResFormAddressScreen { navController.navigate(Routes.RES_FORM_2) }
        }
        composable(Routes.RES_FORM_2) {
            BackHandler { (context as? Activity)?.finish() }
            ResFormEmergencyScreen { navController.navigate(Routes.RES_FORM_3) }
        }
        composable(Routes.RES_FORM_3) {
            BackHandler { (context as? Activity)?.finish() }
            ResFormIdentityScreen { navController.navigate(Routes.RES_FORM_4) }
        }
        composable(Routes.RES_FORM_4) {
            BackHandler { (context as? Activity)?.finish() }
            ResFormPhotoScreen {
                navController.navigate(Routes.HOME_RES) {
                    popUpTo(Routes.RES_FORM_1) { inclusive = true }
                }
            }
        }

        // ── Home ─────────────────────────────────────────────────────────────
        // Back → close app
        composable(Routes.HOME_RES) {
            BackHandler { (context as? Activity)?.finish() }

            LaunchedEffect(Unit) { bookingViewModel.checkPendingConfirmation() }
            val showConfirmation by bookingViewModel.showConfirmation.collectAsState()
            val lastBooking      by bookingViewModel.lastCreatedBooking.collectAsState()
            val bookings         by bookingViewModel.bookings.collectAsState()

            LaunchedEffect(showConfirmation) {
                if (showConfirmation && lastBooking != null) {
                    navController.navigate(Routes.bookingConfirmedRoute(lastBooking!!.id))
                }
            }

            ResidentHomeScreen(
                recentActivities = bookings,
                onNavItemClick   = { navController.navigateMain(it) },
                onBookClick      = { helperId ->
                    navController.navigate(Routes.helperProfileRoute(helperId))
                },
                onCategoryClick  = { category ->
                    navController.navigate(Routes.searchFilteredRoute(category))
                },
                onActivityClick  = { bookingId ->
                    navController.navigate(Routes.bookingDetailsRoute(bookingId))
                }
            )
        }

        // ── Search (unfiltered) ───────────────────────────────────────────────
        composable(Routes.SEARCH) {
            SearchScreen(
                onViewProfile  = { navController.navigate(Routes.helperProfileRoute(it)) },
                onBook         = { navController.navigate(Routes.helperProfileRoute(it)) },
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Search (pre-filtered from category tap) ───────────────────────────
        composable(
            route     = Routes.SEARCH_FILTERED,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = URLDecoder.decode(
                backStackEntry.arguments?.getString("category") ?: "All", "UTF-8"
            )
            SearchScreen(
                initialFilter  = category,
                onViewProfile  = { navController.navigate(Routes.helperProfileRoute(it)) },
                onBook         = { navController.navigate(Routes.helperProfileRoute(it)) },
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Bookings ─────────────────────────────────────────────────────────
        composable(Routes.BOOKINGS) {
            val bookings by bookingViewModel.bookings.collectAsState()
            BookingsScreen(
                bookings        = bookings,
                onNavItemClick  = { navController.navigateMain(it) },
                onDetailsClick  = { bookingId ->
                    navController.navigate(Routes.bookingDetailsRoute(bookingId))
                },
                onChatClick     = { bookingId ->
                    navController.navigate(
                        Routes.chatRoute(bookingId, "Ramesh Kumar", "Cleaning")
                    )
                },
                onActivityClick = { bookingId ->
                    navController.navigate(Routes.activityRoute(bookingId, "Ramesh Kumar"))
                },
                onReviewClick   = { bookingId ->
                    navController.navigate(Routes.feedbackRoute(bookingId, "Ramesh Kumar"))
                }
            )
        }

        // ── Messages ─────────────────────────────────────────────────────────
        composable(Routes.MESSAGES) {
            MessagesScreen(
                onNavItemClick = { navController.navigateMain(it) },
                onThreadClick  = { threadId ->
                    navController.navigate(
                        Routes.chatRoute(threadId, "Ramesh Kumar", "Cleaning")
                    )
                }
            )
        }

        // ── Account ──────────────────────────────────────────────────────────
        composable(Routes.ACCOUNT) {
            ProfileScreen(
                onNavItemClick   = { navController.navigateMain(it) },
                onSettingsClick  = { navController.navigate(Routes.SETTINGS) },
                onMyReviewsClick = { navController.navigate(Routes.MY_REVIEWS) }
            )
        }

        // ── Settings ─────────────────────────────────────────────────────────
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        // ── My Reviews ───────────────────────────────────────────────────────
        composable(Routes.MY_REVIEWS) {
            MyReviewsScreen(onBack = { navController.popBackStack() })
        }

        // ── Helper Profile ────────────────────────────────────────────────────
        composable(
            route     = Routes.HELPER_PROFILE,
            arguments = listOf(navArgument("helperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            HelperProfileScreen(
                helperId  = helperId,
                onBookNow = { navController.navigate(Routes.newBookingRoute("Ramesh Kumar", "Cleaning", 4.9f)) },
                onBack    = { navController.popBackStack() },
                onChat    = {
                    navController.navigate(
                        Routes.chatRoute(helperId, "Ramesh Kumar", "Cleaning")
                    )
                }
            )
        }

        // ── New Booking ───────────────────────────────────────────────────────
        composable(
            route     = Routes.NEW_BOOKING,
            arguments = listOf(
                navArgument("helperName") { type = NavType.StringType },
                navArgument("service")    { type = NavType.StringType },
                navArgument("rating")     { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val helperName = URLDecoder.decode(backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8")
            val service    = URLDecoder.decode(backStackEntry.arguments?.getString("service")    ?: "", "UTF-8")
            val rating     = backStackEntry.arguments?.getFloat("rating") ?: 0f

            NewBookingScreen(
                helperName         = helperName,
                helperService      = service,
                helperRating       = rating,
                onBookingConfirmed = {
                    navController.navigate(Routes.BOOKINGS) {
                        popUpTo(Routes.HOME_RES) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Booking Confirmed ─────────────────────────────────────────────────
        composable(
            route     = Routes.BOOKING_CONFIRMED,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val booking   = bookingViewModel.getBookingById(bookingId)

            BookingConfirmationScreen(
                bookingId   = bookingId,
                helperName  = booking?.helperName  ?: "Helper",
                bookingDate = booking?.bookingDate ?: "",
                bookingTime = booking?.bookingTime ?: "",
                onTimeout   = {
                    bookingViewModel.dismissConfirmation()
                    navController.navigate(Routes.bookingDetailsRoute(bookingId)) {
                        popUpTo(Routes.BOOKING_CONFIRMED) { inclusive = true }
                    }
                }
            )
        }

        // ── Booking Details ───────────────────────────────────────────────────
        composable(
            route     = Routes.BOOKING_DETAILS,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val booking   = bookingViewModel.getBookingById(bookingId)

            BookingDetailsScreen(
                bookingId   = bookingId,
                helperName  = booking?.helperName  ?: "Helper",
                service     = booking?.service     ?: "",
                bookingDate = booking?.bookingDate ?: "",
                bookingTime = booking?.bookingTime ?: "",
                onChat      = {
                    navController.navigate(
                        Routes.chatRoute(bookingId, booking?.helperName ?: "Helper", booking?.service ?: "")
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Chat ──────────────────────────────────────────────────────────────
        composable(
            route     = Routes.CHAT,
            arguments = listOf(
                navArgument("threadId")   { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType },
                navArgument("service")    { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val threadId   = backStackEntry.arguments?.getString("threadId")   ?: ""
            val helperName = URLDecoder.decode(backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8")
            val service    = URLDecoder.decode(backStackEntry.arguments?.getString("service")    ?: "", "UTF-8")

            ChatScreen(
                threadId      = threadId,
                helperName    = helperName,
                service       = service,
                onBack        = { navController.popBackStack() },
                onViewBooking = {
                    navController.navigate(Routes.bookingDetailsRoute(threadId))
                }
            )
        }

        // ── Activity ──────────────────────────────────────────────────────────
        composable(
            route     = Routes.ACTIVITY,
            arguments = listOf(
                navArgument("bookingId")  { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId  = backStackEntry.arguments?.getString("bookingId")  ?: ""
            val helperName = URLDecoder.decode(backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8")
            ActivityScreen(
                bookingId  = bookingId,
                helperName = helperName,
                onBack     = { navController.popBackStack() },
                onChat     = {
                    navController.navigate(Routes.chatRoute(bookingId, helperName, "Cleaning"))
                },
                onCall     = { /* TODO: dial intent */ }
            )
        }

        // ── Feedback ──────────────────────────────────────────────────────────
        composable(
            route     = Routes.FEEDBACK,
            arguments = listOf(
                navArgument("bookingId")  { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookingId  = backStackEntry.arguments?.getString("bookingId")  ?: ""
            val helperName = URLDecoder.decode(backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8")
            FeedbackScreen(
                helperName = helperName,
                bookingId  = bookingId,
                onBack     = { navController.popBackStack() },
                onSubmit   = { navController.popBackStack() }
            )
        }
    }
}
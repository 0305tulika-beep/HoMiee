package com.example.homiee.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homiee.data.local.SessionManager
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.screens.Residentflow.*
import com.example.homiee.ui.screens.auth.*
import com.example.homiee.ui.screens.resident.*
import com.example.homiee.viewmodel.BookingViewModel
import com.example.homiee.viewmodel.BookingViewModelFactory
import com.example.homiee.viewmodel.HelperProfileViewModel
import com.example.homiee.viewmodel.RegisterViewModel
import com.example.homiee.viewmodel.RegisterViewModelFactory
import com.example.homiee.viewmodel.navigation.ResidentOnboardingViewModel
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val SPLASH             = "splash"
    const val SIGNUP_ROUTE       = "signup"
    const val LOGIN_ROUTE        = "login"
    const val OTP_ROUTE          = "otp/{email}/{flow}"   // flow = "login" | "signup"
    const val FORGOT_PASSWORD    = "forgot_password"

    // Resident forms - nested graph + 2 steps
    const val RES_ONBOARDING_GRAPH = "res_onboarding"
    const val RES_FORM_1 = "res_form_address"
    const val RES_FORM_2 = "res_form_photo"

    // Main tabs
    const val HOME_RES        = "home_resident"
    const val SEARCH          = "search"
    const val SEARCH_FILTERED = "search/{category}"
    const val BOOKINGS        = "bookings"
    const val MESSAGES        = "messages"
    const val ACCOUNT         = "account"

    // Sub-screens
    const val MY_REVIEWS         = "my_reviews"
    const val HELPER_PROFILE     = "helper_profile/{helperId}"
    const val HELPER_REVIEWS     = "helper_reviews/{helperId}"

    // Booking flow
    const val NEW_BOOKING       = "new_booking/{helperId}/{helperName}"
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
    fun helperReviewsRoute(helperId: String)     = "helper_reviews/$helperId"
    fun bookingConfirmedRoute(bookingId: String) = "booking_confirmed/$bookingId"
    fun bookingDetailsRoute(bookingId: String)   = "booking_details/$bookingId"
    fun chatRoute(threadId: String, helperName: String, service: String): String {
        val encodedName    = URLEncoder.encode(helperName, "UTF-8")
        val encodedService = URLEncoder.encode(service,    "UTF-8")
        return "chat/$threadId/$encodedName/$encodedService"
    }
    fun newBookingRoute(helperId: String, helperName: String): String {
        val encodedName = URLEncoder.encode(helperName, "UTF-8")
        return "new_booking/$helperId/$encodedName"
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
    if (route == Routes.HOME_RES) {
        // Home is the root of the back stack: just unwind to it
        if (!popBackStack(Routes.HOME_RES, inclusive = false)) {
            navigate(Routes.HOME_RES) { popUpTo(0) }
        }
        return
    }
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

    // When TokenAuthenticator can't recover from a 401 it flips this flag;
    // send the user back to login with a clean back stack.
    val sessionExpired by SessionManager.sessionExpired.collectAsState()
    LaunchedEffect(sessionExpired) {
        if (sessionExpired) {
            SessionManager.consumeSessionExpired()
            navController.navigate(Routes.LOGIN_ROUTE) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

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

        // ── Resident Home ────────────────────────────────────────────────────
        composable(Routes.HOME_RES) {
            BackHandler { (context as? Activity)?.finish() }

            val bookings by bookingViewModel.bookings.collectAsState()
            LaunchedEffect(Unit) { bookingViewModel.refresh() }

            ResidentHomeScreen(
                recentActivities = bookings,
                onNavItemClick = { navController.navigateMain(it) },
                onBookClick = { helperId ->
                    navController.navigate(Routes.helperProfileRoute(helperId))
                },
                onCategoryClick = { category ->
                    navController.navigate(Routes.searchFilteredRoute(category)) {
                        popUpTo(Routes.HOME_RES)      // Home → Search, never Home → Search → Search
                        launchSingleTop = true
                    }
                },
                onActivityClick = { bookingId ->
                    navController.navigate(Routes.bookingDetailsRoute(bookingId))
                }
            )
        }

        // ── Login ────────────────────────────────────────────────────────────
        composable(Routes.LOGIN_ROUTE) {
            BackHandler { (context as? Activity)?.finish() }
            LoginScreen(
                navController = navController,
                onLoginSuccess = { _ ->
                    navController.navigate(Routes.HOME_RES) {
                        popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }

        // ── Forgot Password ───────────────────────────────────────────────────
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onContinue = { _ -> navController.popBackStack() }
            )
        }

        // ── Signup ──────────────────────────────────────────────────────────
        composable(Routes.SIGNUP_ROUTE) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(LocalContext.current)
            )
            SignUpScreen(
                navController = navController,
                viewModel = registerViewModel,
                onSignedUp = { email ->
                    navController.navigate(Routes.otpRoute(email, "signup")) {
                        popUpTo(Routes.SIGNUP_ROUTE) { inclusive = false }
                    }
                }
            )
        }

        // ── OTP ───────────────────────────────────────────────────────────────
        composable(
            route = Routes.OTP_ROUTE,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("flow") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "", "UTF-8"
            )
            val flow = backStackEntry.arguments?.getString("flow") ?: "login"

            OtpScreen(
                email = email,
                onConfirm = {
                    if (flow == "login") {
                        navController.navigate(Routes.HOME_RES) {
                            popUpTo(Routes.LOGIN_ROUTE) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.RES_FORM_1) {
                            popUpTo(Routes.SIGNUP_ROUTE) { inclusive = true }
                        }
                    }
                }
            )
        }

        // ── Resident Onboarding (nested graph, shared ViewModel) ───────────────
        navigation(
            startDestination = Routes.RES_FORM_1,
            route = Routes.RES_ONBOARDING_GRAPH
        ) {
            composable(Routes.RES_FORM_1) { backStackEntry ->
                BackHandler { (context as? Activity)?.finish() }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.RES_ONBOARDING_GRAPH)
                }
                // Owner passed BY NAME - fixes "Argument type mismatch: NavBackStackEntry"
                val vm: ResidentOnboardingViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)

                val formContext = LocalContext.current
                val showErrors by vm.addressShowErrors.collectAsState()
                val loading by vm.addressLoading.collectAsState()
                val error by vm.addressError.collectAsState()

                ResFormAddressScreen(
                    houseNo = vm.houseNo,
                    onHouseNoChange = vm::onHouseNoChange,
                    area = vm.area,
                    onAreaChange = vm::onAreaChange,
                    city = vm.city,
                    onCityChange = vm::onCityChange,
                    pincode = vm.pincode,
                    onPincodeChange = vm::onPincodeChange,
                    onUseCurrentLocation = { lat, lng -> vm.updateLocation(lat, lng) },
                    onNext = {
                        vm.submitAddress(formContext, Routes.RES_FORM_2) {
                            navController.navigate(Routes.RES_FORM_2)
                        }
                    },
                    showValidationError = showErrors,
                    isLoading = loading,
                    errorMessage = error
                )
            }

            composable(Routes.RES_FORM_2) { backStackEntry ->
                BackHandler { navController.popBackStack() }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.RES_ONBOARDING_GRAPH)
                }
                val vm: ResidentOnboardingViewModel =
                    viewModel(viewModelStoreOwner = parentEntry)

                val formContext = LocalContext.current
                val showErrors by vm.photoShowErrors.collectAsState()
                val loading by vm.photoLoading.collectAsState()
                val error by vm.photoError.collectAsState()

                val goHome: () -> Unit = {
                    navController.navigate(Routes.HOME_RES) {
                        popUpTo(Routes.RES_ONBOARDING_GRAPH) { inclusive = true }
                    }
                }

                ResFormPhotoScreen(
                    onFinish = { pickedUri ->
                        vm.submitPhoto(formContext, pickedUri) { goHome() }
                    },
                    onBack = { navController.popBackStack() },
                    onSkip = { vm.skipOnboarding(formContext) { goHome() } },
                    showValidationError = showErrors,
                    isLoading = loading,
                    errorMessage = error
                )
            }
        }

        // ── Search (unfiltered) ───────────────────────────────────────────────
        composable(Routes.SEARCH) {
            SearchScreen(
                initialQuery = "",
                onViewProfile = { id: String -> navController.navigate(Routes.helperProfileRoute(id)) },
                onBook = { id: String -> navController.navigate(Routes.helperProfileRoute(id)) },
                onNavItemClick = { route: String -> navController.navigateMain(route) }
            )
        }

        // ── Search (pre-filled from category tap) ─────────────────────────────
        composable(
            route = Routes.SEARCH_FILTERED,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = URLDecoder.decode(
                backStackEntry.arguments?.getString("category") ?: "", "UTF-8"
            )
            SearchScreen(
                initialQuery = category,
                onViewProfile = { id: String -> navController.navigate(Routes.helperProfileRoute(id)) },
                onBook = { id: String -> navController.navigate(Routes.helperProfileRoute(id)) },
                onNavItemClick = { route: String -> navController.navigateMain(route) }
            )
        }

        // ── Bookings ─────────────────────────────────────────────────────────
        composable(Routes.BOOKINGS) {
            BookingsScreen(
                viewModel = bookingViewModel,
                onNavItemClick = { navController.navigateMain(it) },
                onDetailsClick = { bookingId ->
                    navController.navigate(Routes.bookingDetailsRoute(bookingId))
                },
                onChatClick = { bookingId ->
                    val b = bookingViewModel.getBookingById(bookingId)
                    navController.navigate(
                        Routes.chatRoute(bookingId, b?.helperName ?: "Helper", b?.service ?: "")
                    )
                },
                onActivityClick = { bookingId ->
                    val b = bookingViewModel.getBookingById(bookingId)
                    navController.navigate(Routes.activityRoute(bookingId, b?.helperName ?: "Helper"))
                },
                onReviewClick = { bookingId ->
                    val b = bookingViewModel.getBookingById(bookingId)
                    navController.navigate(Routes.feedbackRoute(bookingId, b?.helperName ?: "Helper"))
                }
            )
        }

        // ── Messages ─────────────────────────────────────────────────────────
        composable(Routes.MESSAGES) {
            MessagesScreen(
                onNavItemClick = { navController.navigateMain(it) },
                onThreadClick = { threadId ->
                    navController.navigate(Routes.chatRoute(threadId, "Ramesh Kumar", "Cleaning"))
                }
            )
        }

        // ── Account ──────────────────────────────────────────────────────────
        composable(Routes.ACCOUNT) {
            ProfileScreen(
                onNavItemClick = { navController.navigateMain(it) },
                onMyReviewsClick = { navController.navigate(Routes.MY_REVIEWS) },
                onLoggedOut = {
                    navController.navigate(Routes.SIGNUP_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── My Reviews ───────────────────────────────────────────────────────
        composable(Routes.MY_REVIEWS) {
            MyReviewsScreen(onBack = { navController.popBackStack() })
        }

        // ── Helper Profile ───────────────────────────────────────────────────
        composable(
            route = Routes.HELPER_PROFILE,
            arguments = listOf(navArgument("helperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            val helperProfileViewModel: HelperProfileViewModel = viewModel(
                key = "helper_profile_$helperId",
                factory = HelperProfileViewModel.Factory(helperId)
            )

            val helper = helperProfileViewModel.helper
            val helperName = helper?.name?.takeIf { it.isNotBlank() } ?: "Helper"
            val helperService = helper?.services?.firstOrNull()?.name
                ?.takeIf { it.isNotBlank() } ?: "Service"

            HelperProfileRoute(
                helperId = helperId,
                viewModel = helperProfileViewModel,
                onBookNow = {
                    navController.navigate(Routes.newBookingRoute(helperId, helperName))
                },
                onBack = { navController.popBackStack() },
                onChat = {
                    navController.navigate(Routes.chatRoute(helperId, helperName, helperService))
                },
                onViewReviews = {
                    navController.navigate(Routes.helperReviewsRoute(helperId))
                }
            )
        }

        // ── Helper Reviews ───────────────────────────────────────────────────
        composable(
            route = Routes.HELPER_REVIEWS,
            arguments = listOf(navArgument("helperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            HelperReviewsScreen(
                helperId = helperId,
                onBack = { navController.popBackStack() }
            )
        }

        // ── New Booking ───────────────────────────────────────────────────────
        composable(
            route = Routes.NEW_BOOKING,
            arguments = listOf(
                navArgument("helperId") { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8"
            )

            // Reuse the profile screen's (already loaded) ViewModel to get this helper's services
            val profileEntry = remember(backStackEntry) {
                runCatching { navController.getBackStackEntry(Routes.HELPER_PROFILE) }.getOrNull()
            }
            val profileViewModel: HelperProfileViewModel = viewModel(
                viewModelStoreOwner = profileEntry ?: backStackEntry,
                key = "helper_profile_$helperId",
                factory = HelperProfileViewModel.Factory(helperId)
            )
            val services = profileViewModel.helper?.services.orEmpty()
                .map { ServiceOption(id = it.serviceId, name = it.name) }   // ← check the id field name

            NewBookingScreen(
                helperId = helperId.toIntOrNull() ?: 0,
                helperName = helperName,
                services = services,
                viewModel = bookingViewModel,            // the shared one, not a new instance
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
            route = Routes.BOOKING_CONFIRMED,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val booking = bookingViewModel.getBookingById(bookingId)

            BookingConfirmationScreen(
                bookingId = bookingId,
                helperName = booking?.helperName ?: "Helper",
                bookingDate = booking?.bookingDate ?: "",
                bookingTime = booking?.bookingTime ?: "",
                totalAmount = booking?.totalAmount?.takeIf { it.isNotBlank() }?.let { "₹$it" } ?: "",
                onTimeout = {
                    bookingViewModel.dismissConfirmation()
                    navController.navigate(Routes.bookingDetailsRoute(bookingId)) {
                        popUpTo(Routes.BOOKING_CONFIRMED) { inclusive = true }
                    }
                }
            )
        }

        // ── Booking Details ───────────────────────────────────────────────────
        composable(
            route = Routes.BOOKING_DETAILS,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""

            BookingDetailsScreen(
                bookingId = bookingId,
                viewModel = bookingViewModel,
                onChat = { b ->
                    navController.navigate(Routes.chatRoute(bookingId, b.helperName, b.service))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Chat ──────────────────────────────────────────────────────────────
        composable(
            route = Routes.CHAT,
            arguments = listOf(
                navArgument("threadId") { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType },
                navArgument("service") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getString("threadId") ?: ""
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8"
            )

            ChatScreen(
                threadId = threadId,
                helperName = helperName,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Activity ──────────────────────────────────────────────────────────
        composable(
            route = Routes.ACTIVITY,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8"
            )
            ActivityScreen(
                helperName = helperName,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Feedback ──────────────────────────────────────────────────────────
        composable(
            route = Routes.FEEDBACK,
            arguments = listOf(
                navArgument("bookingId") { type = NavType.StringType },
                navArgument("helperName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "", "UTF-8"
            )
            FeedbackScreen(
                helperName = helperName,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }
    }
}
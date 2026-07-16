package com.example.homiee.navigation

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.homiee.ui.components.NavTab
import com.example.homiee.ui.screens.Residentflow.*
import com.example.homiee.ui.screens.auth.*
import com.example.homiee.ui.screens.resident.*
import com.example.homiee.viewmodel.BookingViewModel
import com.example.homiee.viewmodel.BookingViewModelFactory
import com.example.homiee.viewmodel.RegisterViewModel
import com.example.homiee.viewmodel.navigation.ResidentOnboardingViewModel
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val SPLASH             = "splash"
    const val SIGNUP_ROUTE       = "signup"
    const val LOGIN_ROUTE        = "login"
    const val OTP_ROUTE          = "otp/{email}/{flow}"   // flow = "login" | "signup"
    const val FORGOT_PASSWORD    = "forgot_password"

    // Resident forms — nested graph + 2 steps
    // (Emergency Contact + its OTP step, and Identity form, both removed)
    const val RES_ONBOARDING_GRAPH = "res_onboarding"     // parent graph route
    const val RES_FORM_1 = "res_form_address"
    const val RES_FORM_2 = "res_form_photo"               // was RES_FORM_3

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
    const val VERIFIED_DOCUMENTS = "verified_documents/{helperId}"
    const val DOCUMENT_VIEWER    = "document_viewer/{helperId}/{documentType}"

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
    fun helperReviewsRoute(helperId: String)     = "helper_reviews/$helperId"
    fun verifiedDocumentsRoute(helperId: String) = "verified_documents/$helperId"
    fun documentViewerRoute(helperId: String, documentType: VerifiedDocumentType): String =
        "document_viewer/$helperId/${documentType.name}"
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

    // NEW: whenever any API call 401s with an expired/invalid token,
    // TokenAuthenticator clears the session and flips this flag — react by
    // sending the user cleanly back to login instead of leaving them stuck
    // on a screen where every request silently keeps failing.
    val sessionExpired by com.example.homiee.data.local.SessionManager.sessionExpired.collectAsState()
    androidx.compose.runtime.LaunchedEffect(sessionExpired) {
        if (sessionExpired) {
            com.example.homiee.data.local.SessionManager.consumeSessionExpired()
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

            ResidentHomeScreen(
                recentActivities = bookings,
                onNavItemClick = { navController.navigateMain(it) },
                onBookClick = { helperId ->
                    navController.navigate(Routes.helperProfileRoute(helperId))
                },
                onCategoryClick = { category ->
                    navController.navigate(Routes.searchFilteredRoute(category))
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
                onContinue = { _ ->
                    navController.popBackStack()
                }
            )
        }

        // ── Signup ──────────────────────────────────────────────────────────
        composable(Routes.SIGNUP_ROUTE) {
            val registerViewModel: RegisterViewModel = viewModel()
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

        // ── OTP (signup / login email verification) ───────────────────────────
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
        // 2 steps: Address -> Photo
        // (Emergency Contact + its OTP verification, and Identity form, removed)
        navigation(
            startDestination = Routes.RES_FORM_1,
            route = Routes.RES_ONBOARDING_GRAPH
        ) {
            composable(Routes.RES_FORM_1) { backStackEntry ->
                BackHandler { (context as? Activity)?.finish() }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.RES_ONBOARDING_GRAPH)
                }
                val vm: ResidentOnboardingViewModel = viewModel(parentEntry)
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
                    onUseCurrentLocation = { lat, lng ->
                        vm.updateLocation(lat, lng)
                    },
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

            // NOTE: Emergency Contact form + its OTP verification step, and the
            // Identity (Aadhaar/PAN) form, have all been removed per request.
            // ResFormEmergencyScreen.kt, ResFormEmergencyOtpScreen.kt, and
            // ResFormIdentityScreen.kt are no longer referenced anywhere and
            // can be deleted from the project.

            composable(Routes.RES_FORM_2) { backStackEntry ->
                BackHandler { navController.popBackStack() }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Routes.RES_ONBOARDING_GRAPH)
                }
                val vm: ResidentOnboardingViewModel = viewModel(parentEntry)
                val formContext = LocalContext.current
                val showErrors by vm.photoShowErrors.collectAsState()
                val loading by vm.photoLoading.collectAsState()
                val error by vm.photoError.collectAsState()

                // Pure navigation — TokenManager.markFormsCompleted() is now called
                // from inside the ViewModel (submitPhoto on success, or
                // skipOnboarding), which is the actual source of truth Splash reads.
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
                    onSkip = {
                        // Pure local skip: no API call, just marks onboarding
                        // complete and goes home immediately.
                        vm.skipOnboarding(formContext) { goHome() }
                    },
                    showValidationError = showErrors,
                    isLoading = loading,
                    errorMessage = error
                )
            }
        } // ← closes navigation(RES_ONBOARDING_GRAPH)

        // ── Search (unfiltered) ───────────────────────────────────────────────
        composable(Routes.SEARCH) {
            SearchScreen(
                onViewProfile = { navController.navigate(Routes.helperProfileRoute(it)) },
                onBook = { navController.navigate(Routes.helperProfileRoute(it)) },
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Search (pre-filtered from category tap) ───────────────────────────
        composable(
            route = Routes.SEARCH_FILTERED,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = URLDecoder.decode(
                backStackEntry.arguments?.getString("category") ?: "All", "UTF-8"
            )
            SearchScreen(
                initialFilter = category,
                onViewProfile = { navController.navigate(Routes.helperProfileRoute(it)) },
                onBook = { navController.navigate(Routes.helperProfileRoute(it)) },
                onNavItemClick = { navController.navigateMain(it) }
            )
        }

        // ── Bookings ─────────────────────────────────────────────────────────
        composable(Routes.BOOKINGS) {
            val bookings by bookingViewModel.bookings.collectAsState()
            BookingsScreen(
                bookings = bookings,
                onNavItemClick = { navController.navigateMain(it) },
                onDetailsClick = { bookingId ->
                    navController.navigate(Routes.bookingDetailsRoute(bookingId))
                },
                onChatClick = { bookingId ->
                    navController.navigate(
                        Routes.chatRoute(bookingId, "Ramesh Kumar", "Cleaning")
                    )
                },
                onActivityClick = { bookingId ->
                    navController.navigate(Routes.activityRoute(bookingId, "Ramesh Kumar"))
                },
                onReviewClick = { bookingId ->
                    navController.navigate(Routes.feedbackRoute(bookingId, "Ramesh Kumar"))
                }
            )
        }

        // ── Messages ─────────────────────────────────────────────────────────
        composable(Routes.MESSAGES) {
            MessagesScreen(
                onNavItemClick = { navController.navigateMain(it) },
                onThreadClick = { threadId ->
                    navController.navigate(
                        Routes.chatRoute(threadId, "Ramesh Kumar", "Cleaning")
                    )
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
                        popUpTo(0) { inclusive = true }   // clear entire back stack
                    }
                }
            )
        }

        // ── My Reviews ───────────────────────────────────────────────────────
        composable(Routes.MY_REVIEWS) {
            MyReviewsScreen(onBack = { navController.popBackStack() })
        }

        // ── Helper Profile ────────────────────────────────────────────────────
        composable(
            route = Routes.HELPER_PROFILE,
            arguments = listOf(navArgument("helperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            HelperProfileScreen(
                helperId = helperId,
                onBookNow = {
                    navController.navigate(
                        Routes.newBookingRoute(
                            "Ramesh Kumar",
                            "Cleaning",
                            4.9f
                        )
                    )
                },
                onBack = { navController.popBackStack() },
                onChat = {
                    navController.navigate(
                        Routes.chatRoute(helperId, "Ramesh Kumar", "Cleaning")
                    )
                },
                onViewVerifiedDocuments = {
                    navController.navigate(Routes.verifiedDocumentsRoute(helperId))
                },
                onViewReviews = {
                    navController.navigate(Routes.helperReviewsRoute(helperId))
                }
            )
        }

        // ── Helper Reviews (full reviews list) ──────────────────────────────────
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

        // ── Verified Documents (list) ───────────────────────────────────────────
        composable(
            route = Routes.VERIFIED_DOCUMENTS,
            arguments = listOf(navArgument("helperId") { type = NavType.StringType })
        ) { backStackEntry ->
            val helperId = backStackEntry.arguments?.getString("helperId") ?: ""
            VerifiedDocumentsScreen(
                onBack = { navController.popBackStack() },
                onDocumentClick = { documentType ->
                    navController.navigate(
                        Routes.documentViewerRoute(helperId, documentType)
                    )
                }
            )
        }

        // ── Document Viewer (single document) ───────────────────────────────────
        composable(
            route = Routes.DOCUMENT_VIEWER,
            arguments = listOf(
                navArgument("helperId") { type = NavType.StringType },
                navArgument("documentType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val documentTypeArg = backStackEntry.arguments?.getString("documentType")
                ?: VerifiedDocumentType.GOVERNMENT_ID.name
            val documentType = VerifiedDocumentType.valueOf(documentTypeArg)

            DocumentViewerScreen(
                documentType = documentType,
                documentImageUrl = null, // TODO: wire real URL from helper data once backend provides it
                isVerified = true,
                onBack = { navController.popBackStack() }
            )
        }

        // ── New Booking ───────────────────────────────────────────────────────
        composable(
            route = Routes.NEW_BOOKING,
            arguments = listOf(
                navArgument("helperName") { type = NavType.StringType },
                navArgument("service") { type = NavType.StringType },
                navArgument("rating") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "",
                "UTF-8"
            )
            val service =
                URLDecoder.decode(backStackEntry.arguments?.getString("service") ?: "", "UTF-8")
            val rating = backStackEntry.arguments?.getFloat("rating") ?: 0f

            NewBookingScreen(
                helperName = helperName,
                helperService = service,
                helperRating = rating,
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
            val booking = bookingViewModel.getBookingById(bookingId)

            BookingDetailsScreen(
                bookingId = bookingId,
                helperName = booking?.helperName ?: "Helper",
                service = booking?.service ?: "",
                bookingDate = booking?.bookingDate ?: "",
                bookingTime = booking?.bookingTime ?: "",
                address = booking?.address ?: "",
                onChat = {
                    navController.navigate(
                        Routes.chatRoute(
                            bookingId,
                            booking?.helperName ?: "Helper",
                            booking?.service ?: ""
                        )
                    )
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
                backStackEntry.arguments?.getString("helperName") ?: "",
                "UTF-8"
            )
            val service =
                URLDecoder.decode(backStackEntry.arguments?.getString("service") ?: "", "UTF-8")

            ChatScreen(
                threadId = threadId,
                helperName = helperName,
                onBack = { navController.popBackStack() },
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
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "",
                "UTF-8"
            )
            ActivityScreen(
                helperName = helperName,
                onBack = { navController.popBackStack() },
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
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            val helperName = URLDecoder.decode(
                backStackEntry.arguments?.getString("helperName") ?: "",
                "UTF-8"
            )
            FeedbackScreen(
                helperName = helperName,
                onBack = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }
    }
}
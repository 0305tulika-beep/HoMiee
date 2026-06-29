package com.example.homiee.ui.screens.Residentflow

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

@Preview(showBackground = true, showSystemUi = true, name = "Resident Home")
@Composable fun ResidentHomePreview() {
    HomieeTheme {
        ResidentHomeScreen(
            recentActivities = emptyList(),
            onNavItemClick   = {},
            onCategoryClick  = {},
            onActivityClick  = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Search Screen")
@Composable fun SearchScreenPreview() {
    HomieeTheme { SearchScreen(onViewProfile = {}, onBook = {}, onNavItemClick = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Bookings Screen")
@Composable fun BookingsScreenPreview() {
    HomieeTheme {
        BookingsScreen(
            bookings        = emptyList(),
            onNavItemClick  = {},
            onDetailsClick  = {},
            onChatClick     = {},
            onActivityClick = {},
            onReviewClick   = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Messages Screen")
@Composable fun MessagesScreenPreview() {
    HomieeTheme { MessagesScreen(onNavItemClick = {}, onThreadClick = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Chat Screen")
@Composable fun ChatScreenPreview() {
    HomieeTheme {
        ChatScreen(
            threadId      = "t001",
            helperName    = "Ramesh Kumar",
            service       = "Cleaning",
            onBack        = {},
            onViewBooking = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Activity Screen")
@Composable fun ActivityScreenPreview() {
    HomieeTheme {
        ActivityScreen(
            bookingId  = "b001",
            helperName = "Ramesh Kumar",
            onBack     = {},
            onChat     = {},
            onCall     = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Feedback Screen")
@Composable fun FeedbackScreenPreview() {
    HomieeTheme {
        FeedbackScreen(
            helperName = "Ramesh Kumar",
            bookingId  = "b001",
            onBack     = {},
            onSubmit   = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Profile Screen")
@Composable fun ProfileScreenPreview() {
    HomieeTheme { ProfileScreen(onNavItemClick = {}, onSettingsClick = {}, onMyReviewsClick = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Settings Screen")
@Composable fun SettingsScreenPreview() {
    HomieeTheme { SettingsScreen(onBack = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "My Reviews Screen")
@Composable fun MyReviewsScreenPreview() {
    HomieeTheme { MyReviewsScreen(onBack = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Helper Profile Screen")
@Composable fun HelperProfileScreenPreview() {
    HomieeTheme { HelperProfileScreen(helperId = "test_001", onBookNow = {}, onBack = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "New Booking")
@Composable fun NewBookingScreenPreview() {
    HomieeTheme {
        NewBookingScreen(
            helperName         = "Ramesh Kumar",
            helperService      = "House Cleaning",
            helperRating       = 4.9f,
            onBookingConfirmed = {},
            onBack             = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Booking Confirmation")
@Composable fun BookingConfirmationScreenPreview() {
    HomieeTheme {
        BookingConfirmationScreen(
            bookingId   = "hsdgfsgd",
            helperName  = "Ramesh Kumar",
            bookingDate = "Jun 12, 2026",
            bookingTime = "10:00 AM",
            onTimeout   = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Booking Details")
@Composable fun BookingDetailsScreenPreview() {
    HomieeTheme {
        BookingDetailsScreen(
            bookingId   = "dfgfjhadg",
            helperName  = "Ramesh Kumar",
            service     = "Cleaning",
            bookingDate = "Jun 12, 2026",
            bookingTime = "10:00 AM",
            onBack      = {}
        )
    }
}
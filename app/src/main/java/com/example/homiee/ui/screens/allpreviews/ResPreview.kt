package com.example.homiee.ui.screens.allpreviews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.screens.Residentflow.ActivityScreen
import com.example.homiee.ui.screens.Residentflow.BookingConfirmationScreen
import com.example.homiee.ui.screens.Residentflow.BookingDetailsScreen
import com.example.homiee.ui.screens.Residentflow.BookingsScreen
import com.example.homiee.ui.screens.Residentflow.ChatScreen
import com.example.homiee.ui.screens.Residentflow.FeedbackScreen
import com.example.homiee.ui.screens.Residentflow.HelperProfileScreen
import com.example.homiee.ui.screens.Residentflow.MessagesScreen
import com.example.homiee.ui.screens.Residentflow.MyReviewsScreen
import com.example.homiee.ui.screens.Residentflow.NewBookingScreen
import com.example.homiee.ui.screens.Residentflow.ProfileScreen
import com.example.homiee.ui.screens.Residentflow.ResidentHomeScreen
import com.example.homiee.ui.screens.Residentflow.SearchScreen
import com.example.homiee.ui.theme.HomieeTheme
import com.example.homiee.ui.screens.Residentflow.VerifiedDocumentsScreen
import com.example.homiee.ui.screens.Residentflow.DocumentViewerScreen
import com.example.homiee.ui.screens.Residentflow.VerifiedDocumentType
import com.example.homiee.ui.screens.Residentflow.HelperReviewsScreen

@Preview(showBackground = true, showSystemUi = true, name = "Resident Home")
@Composable fun ResidentHomePreview() {
    HomieeTheme {
        ResidentHomeScreen(
            recentActivities = emptyList(),
            onNavItemClick = {},
            onCategoryClick = {},
            onActivityClick = {}
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
            bookings = emptyList(),
            onNavItemClick = {},
            onDetailsClick = {},
            onChatClick = {},
            onActivityClick = {},
            onReviewClick = {}
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
            threadId = "t001",
            helperName = "Ramesh Kumar",
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Activity Screen")
@Composable fun ActivityScreenPreview() {
    HomieeTheme {
        ActivityScreen(
            helperName = "Ramesh Kumar",
            onBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Feedback Screen")
@Composable fun FeedbackScreenPreview() {
    HomieeTheme {
        FeedbackScreen(
            helperName = "Ramesh Kumar",
            onBack = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Profile Screen")
@Composable fun ProfileScreenPreview() {
    HomieeTheme {
        ProfileScreen(
            onNavItemClick = {},
            onMyReviewsClick = {},
            onLoggedOut = {}
        )
    }
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
            helperName = "Ramesh Kumar",
            helperService = "House Cleaning",
            helperRating = 4.9f,
            onBookingConfirmed = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Booking Confirmation")
@Composable fun BookingConfirmationScreenPreview() {
    HomieeTheme {
        BookingConfirmationScreen(
            bookingId = "hsdgfsgd",
            helperName = "Ramesh Kumar",
            bookingDate = "Jun 12, 2026",
            bookingTime = "10:00 AM",
            onTimeout = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Booking Details")
@Composable fun BookingDetailsScreenPreview() {
    HomieeTheme {
        BookingDetailsScreen(
            bookingId = "dfgfjhadg",
            helperName = "Ramesh Kumar",
            service = "Cleaning",
            bookingDate = "Jun 12, 2026",
            bookingTime = "10:00 AM",
            onBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Verified Documents")
@Composable fun VerifiedDocumentsScreenPreview() {
    HomieeTheme {
        VerifiedDocumentsScreen(
            onBack = {},
            onDocumentClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Document Viewer - Government ID")
@Composable fun DocumentViewerGovIdPreview() {
    HomieeTheme {
        DocumentViewerScreen(
            documentType = VerifiedDocumentType.GOVERNMENT_ID,
            documentImageUrl = null,
            isVerified = true,
            onBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Document Viewer - Police Verification")
@Composable fun DocumentViewerPoliceVerificationPreview() {
    HomieeTheme {
        DocumentViewerScreen(
            documentType = VerifiedDocumentType.POLICE_VERIFICATION,
            documentImageUrl = null,
            isVerified = true,
            onBack = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Helper Reviews Screen")
@Composable fun HelperReviewsScreenPreview() {
    HomieeTheme { HelperReviewsScreen(helperId = "test_001", onBack = {}) }
}
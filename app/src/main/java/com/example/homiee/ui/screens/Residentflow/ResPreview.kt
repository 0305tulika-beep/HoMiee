package com.example.homiee.ui.screens.Residentflow

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

@Preview(showBackground = true, showSystemUi = true, name = "Resident Home")
@Composable
fun ResidentHomePreview() {
    HomieeTheme { ResidentHomeScreen(onNavItemClick = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Search Screen")
@Composable
fun SearchScreenPreview() {
    HomieeTheme {
        SearchScreen(
            onViewProfile  = {},
            onBook         = {},
            onNavItemClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Bookings Screen")
@Composable
fun BookingsScreenPreview() {
    HomieeTheme { BookingsScreen(onNavItemClick = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Profile Screen")
@Composable
fun ProfileScreenPreview() {
    HomieeTheme {
        ProfileScreen(
            onNavItemClick   = {},
            onSettingsClick  = {},
            onMyReviewsClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Settings Screen")
@Composable
fun SettingsScreenPreview() {
    HomieeTheme { SettingsScreen(onBack = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "My Reviews Screen")
@Composable
fun MyReviewsScreenPreview() {
    HomieeTheme { MyReviewsScreen(onBack = {}) }
}

@Preview(showBackground = true, showSystemUi = true, name = "Helper Profile Screen")
@Composable
fun HelperProfileScreenPreview() {
    HomieeTheme {
        HelperProfileScreen(
            helperId  = "test_001",
            onBookNow = {},
            onBack    = {}
        )
    }
}
package com.example.homiee.ui.screens.Residentflow

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.homiee.ui.theme.HomieeTheme

@Preview(showBackground = true, showSystemUi = true, name = "Resident Home")
@Composable
fun ResidentHomePreview() {
    HomieeTheme { ResidentHomeScreen() }
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
    HomieeTheme {
        BookingsScreen(onNavItemClick = {})
    }
}
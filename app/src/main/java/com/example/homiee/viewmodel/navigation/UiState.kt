package com.example.homiee.viewmodel

// ============================================================
// A single reusable UI state shape for any screen that just
// needs to track: is it loading? did it succeed? what error?
//
// Each ViewModel below exposes one of these per screen so your
// Composable can react to it (show a spinner, navigate, show
// an error message, etc).
// ============================================================
data class UiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
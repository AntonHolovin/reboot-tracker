package com.holovin.reboottracker.ui.screens.main

import java.time.Instant

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState

    data class Content(val reboots: List<Instant>) : MainScreenUiState
}
package com.example.easygame.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Home : Screen

    @Serializable
    data object GameDetail : Screen

    @Serializable
    data object HighScore : Screen

    @Serializable
    data object Settings : Screen
}

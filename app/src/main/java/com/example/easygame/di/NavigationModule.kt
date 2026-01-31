package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import com.example.easygame.ui.screen.game_detail.GameDetailScreen
import com.example.easygame.ui.screen.game_detail.GameDetailViewModel
import com.example.easygame.ui.screen.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {

    navigation<Screen.Home> {
        HomeScreen(
            navigateToGameDetail = {
                get<Navigator>().navigateTo(destination = Screen.GameDetail)
            },
            navigateToHighScore = {
                get<Navigator>().navigateTo(destination = Screen.HighScore)
            },
            navigateToSettings = {
                get<Navigator>().navigateTo(destination = Screen.Settings)
            },
            quitGame = {
                get<Navigator>().onQuitApplication?.invoke()
            }
        )
    }

    navigation<Screen.GameDetail> {
        val navigator = get<Navigator>()
        GameDetailScreen(
            viewModel = koinViewModel<GameDetailViewModel>(),
            onBack = { navigator.popBackStack() }
        )
    }
}

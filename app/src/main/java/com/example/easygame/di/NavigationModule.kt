package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import com.example.easygame.ui.screen.game_detail.GameDetailScreen
import com.example.easygame.ui.screen.high_score.HighScoreScreen
import com.example.easygame.ui.screen.home.HomeScreen
import com.example.easygame.ui.screen.setting.SettingScreen
import com.example.easygame.ui.screen.store.StoreScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {

    navigation<Screen.Home> {
        val navigator = getOrNull<Navigator>()
        HomeScreen(
            navigateToGameDetail = {
                navigator?.navigateTo(destination = Screen.GameDetail)
            },
            navigateToHighScore = {
                navigator?.navigateTo(destination = Screen.HighScore)
            },
            navigateToStore = {
                navigator?.navigateTo(destination = Screen.Store)
            },
            navigateToSettings = {
                navigator?.navigateTo(destination = Screen.Setting)
            },
            quitGame = {
                navigator?.onQuitApplication?.invoke()
            }
        )
    }

    navigation<Screen.GameDetail> {
        val navigator = getOrNull<Navigator>()
        GameDetailScreen(
            viewModel = koinViewModel(),
            onBack = { navigator?.popBackStack() }
        )
    }

    navigation<Screen.Store> {
        val navigator = getOrNull<Navigator>()
        StoreScreen(
            viewModel = koinViewModel(),
            onBack = { navigator?.popBackStack() }
        )
    }

    navigation<Screen.HighScore> {
        val navigator = getOrNull<Navigator>()
        HighScoreScreen(
            viewModel = koinViewModel(),
            onBack = { navigator?.popBackStack() }
        )
    }

    navigation<Screen.Setting> {
        SettingScreen()
    }
}

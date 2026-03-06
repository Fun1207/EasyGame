package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import com.example.easygame.ui.screen.game_detail.GameDetailScreen
import com.example.easygame.ui.screen.high_score.HighScoreScreen
import com.example.easygame.ui.screen.home.HomeScreen
import com.example.easygame.ui.screen.setting.SettingScreen
import com.example.easygame.ui.screen.store.StoreScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.koin.dsl.onClose

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {

    single {
        Navigator(
            startDestination = Screen.Home,
            CoroutineScope(Dispatchers.Main + SupervisorJob())
        )
    }.onClose { navigator ->
        navigator?.onClear()
    }

    navigation<Screen.Home> {
        val navigator = koinInject<Navigator>()
        HomeScreen(
            navigateToGameDetail = {
                navigator.navigateTo(destination = Screen.GameDetail)
            },
            navigateToHighScore = {
                navigator.navigateTo(destination = Screen.HighScore)
            },
            navigateToStore = {
                navigator.navigateTo(destination = Screen.Store)
            },
            navigateToSettings = {
                navigator.navigateTo(destination = Screen.Setting)
            },
            quitGame = {
                navigator.onQuitApplication?.invoke()
            }
        )
    }

    navigation<Screen.GameDetail> {
        val navigator = koinInject<Navigator>()
        GameDetailScreen(
            viewModel = koinViewModel(),
            onBack = { navigator.popBackStack() }
        )
    }

    navigation<Screen.Store> {
        val navigator = koinInject<Navigator>()
        StoreScreen(
            viewModel = koinViewModel(),
            onBack = { navigator.popBackStack() }
        )
    }

    navigation<Screen.HighScore> {
        val navigator = koinInject<Navigator>()
        HighScoreScreen(
            viewModel = koinViewModel(),
            onBack = { navigator.popBackStack() }
        )
    }

    navigation<Screen.Setting> {
        val navigator = koinInject<Navigator>()
        SettingScreen(
            viewModel = koinViewModel(),
            onBack = { navigator.popBackStack() }
        )
    }
}

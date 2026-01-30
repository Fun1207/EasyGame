package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import com.example.easygame.ui.screen.home.HomeScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {

    single { Navigator(startDestination = Screen.Home) }

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
}

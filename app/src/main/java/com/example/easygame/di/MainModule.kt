package com.example.easygame.di

import com.example.easygame.data.repository.GameSensorManager
import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import com.example.easygame.ui.screen.game_detail.GameDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val mainModule = module {
    single { GameSensorManager(context = androidContext()) }
    single { Navigator(startDestination = Screen.Home) }
    viewModel { GameDetailViewModel(get()) }
    activityRetainedScope {
        scoped { Navigator(startDestination = Screen.Home) }
    }
}

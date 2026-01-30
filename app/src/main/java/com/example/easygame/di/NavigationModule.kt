package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import org.koin.dsl.module

val navigationModule = module {

    single { Navigator(startDestination = Screen.Home) }

}

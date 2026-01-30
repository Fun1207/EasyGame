package com.example.easygame.di

import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.dsl.module

val mainModule = module {
    includes(navigationModule)
    activityRetainedScope {
        scoped { Navigator(startDestination = Screen.Home) }
    }
}

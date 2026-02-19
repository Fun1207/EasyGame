package com.example.easygame.di

import coil3.ImageLoader
import com.example.easygame.data.local.datastore.CoinDataStore
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.data.repository.GameResourceRepository
import com.example.easygame.data.repository.GameSensorManager
import com.example.easygame.domain.usecase.BuyItemUseCase
import com.example.easygame.domain.usecase.GetItemUseCase
import com.example.easygame.navigation.Navigator
import com.example.easygame.navigation.Screen
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singletonModule = module {
    single { GameSensorManager(context = androidContext()) }
    single { Navigator(startDestination = Screen.Home) }
    single { FirebaseDataSource() }
    single { ImageLoader.Builder(androidContext()).build() }
    single { GameResourceRepository(androidContext(), get()) }
    single { BuyItemUseCase(get(), get(), get()) }
    single { GetItemUseCase(get(), get()) }
    single { CoinDataStore(androidContext()) }
}

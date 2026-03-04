package com.example.easygame.di

import coil3.ImageLoader
import com.example.easygame.data.local.datastore.GameDataStore
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.data.repository.GameResourceRepository
import com.example.easygame.data.repository.GameSensorManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singletonModule = module {
    single { GameSensorManager(context = androidContext()) }
    single { FirebaseDataSource() }
    single { ImageLoader.Builder(androidContext()).build() }
    single { GameResourceRepository(androidContext(), get()) }
    single { GameDataStore(androidContext()) }
}

package com.example.easygame.di

import androidx.room.Room
import com.example.easygame.data.local.database.EasyGameDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            EasyGameDatabase::class.java,
            "easy-game-db"
        ).build()
    }

    single { get<EasyGameDatabase>().purchasedObjectDao() }
    single { get<EasyGameDatabase>().selectedItemDao() }
    single { get<EasyGameDatabase>().highScoreDao() }
}

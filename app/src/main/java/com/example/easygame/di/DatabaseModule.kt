package com.example.easygame.di

import androidx.room.Room
import com.example.easygame.data.local.database.EasyGameDatabase
import com.example.easygame.domain.util.GAME_DATABASE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            EasyGameDatabase::class.java,
            GAME_DATABASE
        ).build()
    }

    single { get<EasyGameDatabase>().purchasedObjectDao() }
    single { get<EasyGameDatabase>().highScoreDao() }
}

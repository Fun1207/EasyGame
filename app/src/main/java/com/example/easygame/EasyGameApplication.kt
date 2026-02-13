package com.example.easygame

import android.app.Application
import com.example.easygame.di.databaseModule
import com.example.easygame.di.navigationModule
import com.example.easygame.di.singletonModule
import com.example.easygame.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EasyGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(singletonModule, viewModelModule, navigationModule, databaseModule)
            androidContext(this@EasyGameApplication)
            androidLogger()
        }
    }
}

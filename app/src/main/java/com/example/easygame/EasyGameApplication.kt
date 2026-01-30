package com.example.easygame

import android.app.Application
import com.example.easygame.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EasyGameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(mainModule)
            androidContext(this@EasyGameApplication)
            androidLogger()
        }
    }
}

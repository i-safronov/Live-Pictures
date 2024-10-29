package com.safronov.livepictures.app

import android.app.Application
import com.safronov.livepictures.di.appModule
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}
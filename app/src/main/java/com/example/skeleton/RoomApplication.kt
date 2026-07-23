package com.example.Aqi

import android.app.Application
import com.example.Aqi.BuildConfig
import com.example.skeleton.Model.Koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class RoomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@RoomApplication)
            modules(appModule)
        }

        // This is the "Professional" way:
        // Only plant the DebugTree if the app is in Debug mode.
        // This ensures your logs NEVER show up in the Play Store version.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

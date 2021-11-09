package com.troido.bless.app

import android.app.Application
import com.troido.bless.Bless
import com.troido.bless.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Bless.initialize(this)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SampleApplication)
            modules(appModule)
        }
    }
}

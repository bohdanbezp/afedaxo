package com.afedaxo

import android.app.Application
import com.afedaxo.di.mvvmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AfedaxoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@AfedaxoApp)
            // declare modules
            modules(mvvmModule)
        }
    }
}
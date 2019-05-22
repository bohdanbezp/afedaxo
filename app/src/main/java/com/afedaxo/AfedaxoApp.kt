package com.afedaxo

import android.app.Application
import com.afedaxo.di.AppComponent
import com.afedaxo.di.AppModule
import com.afedaxo.di.DaggerAppComponent

class AfedaxoApp : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}
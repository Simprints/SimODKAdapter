package com.simprints.simodkadapter

import android.app.Application
import com.simprints.simodkadapter.di.koinModules
import org.koin.android.ext.android.startKoin


class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(androidContext = this, modules = listOf(koinModules))
    }

}
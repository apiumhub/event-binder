package com.apiumhub.events.sample

import android.app.Application
import com.apiumhub.events.sample.binding.globalBinds

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        globalBinds()
    }

}
package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.globalBinds

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        globalBinds()
    }

}
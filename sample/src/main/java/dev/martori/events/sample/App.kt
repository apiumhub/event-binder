package dev.martori.events.sample

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}

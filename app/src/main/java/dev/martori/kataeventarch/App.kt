package dev.martori.kataeventarch

import android.app.Application
import dev.martori.kataeventarch.binding.MainService
import dev.martori.kataeventarch.domain.MainDelayService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(mainModule)
        }
    }

}

private val mainModule = module {
    single<MainService> { MainDelayService() }
}

package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.CounterService
import dev.martori.events.sample.binding.MainService
import dev.martori.events.sample.domain.CounterServiceImpl
import dev.martori.events.sample.domain.MainDelayService
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
    single<CounterService> { CounterServiceImpl() }
}

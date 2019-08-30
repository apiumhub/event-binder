package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.services.CounterService
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.services.MainService
import dev.martori.events.sample.data.inmemory.InMemoryDetailsService
import dev.martori.events.sample.domain.services.DelayedCounterService
import dev.martori.events.sample.domain.services.MainDelayService
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
    single<CounterService> { DelayedCounterService() }
    single<DetailsService> { InMemoryDetailsService() }
}

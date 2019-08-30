package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.services.CounterService
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.services.MainService
import dev.martori.events.sample.data.inmemory.InMemoryCounterRepository
import dev.martori.events.sample.data.inmemory.InMemoryDetailsService
import dev.martori.events.sample.data.network.ktor.KtorDetailsApi
import dev.martori.events.sample.domain.repositories.CounterRepository
import dev.martori.events.sample.domain.services.DelayedCounterService
import dev.martori.events.sample.domain.services.MainDelayService
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.host
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    services,
                    repositories,
                    ktor
                )
            )
        }
    }

}

private val services = module {
    single<MainService> { MainDelayService() }
    single<CounterService> { DelayedCounterService(get()) }
    single<DetailsService> { InMemoryDetailsService() }
}

private val repositories = module {
    single<CounterRepository> { InMemoryCounterRepository() }
}

private val ktor = module {
    single {
        HttpClient {
            defaultRequest {
                host = "127.0.0.1"
            }
        }
    }
    single { KtorDetailsApi(get()) }
}
package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.binds.bindDetailsNavigation
import dev.martori.events.sample.binding.binds.bindDetailsService
import dev.martori.events.sample.binding.binds.bindListNavigation
import dev.martori.events.sample.binding.binds.bindLoadElementsService
import dev.martori.events.sample.binding.services.*
import dev.martori.events.sample.binding.views.DetailViewModel
import dev.martori.events.sample.data.inmemory.InMemoryCounterRepository
import dev.martori.events.sample.data.network.api.DetailsApi
import dev.martori.events.sample.data.network.ktor.KtorDetailsApi
import dev.martori.events.sample.domain.repositories.CounterRepository
import dev.martori.events.sample.domain.services.*
import dev.martori.events.sample.ui.DetailsFragment
import dev.martori.events.sample.ui.MainListFragment
import dev.martori.events.sample.ui.koinBind
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val services = module {
    single<MainService> { MainDelayService() }
    single<CounterService> { DelayedCounterService(get()) }
    single<DetailsService> { RemoteDetailsService(get()) }
    single<LoadElementsService> { MockLoadElementsService() }
    single<ErrorLogger<DetailViewModel>> { AndroidErrorLogger() }
}

private val repositories = module {
    single<CounterRepository> { InMemoryCounterRepository() }
}

private val ktor = module {
    single {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                host = "127.0.0.1:8080"
            }
        }
    }
    single<DetailsApi> { KtorDetailsApi(get()) }
}

private val binds = module {
    koinBind<DetailsFragment> { details ->
        details.bindDetailsService(details, get())
        details.bindDetailsNavigation(details, get())
    }
    koinBind<MainListFragment> { mainList ->
        mainList.bindListNavigation(mainList, get())
        mainList.bindLoadElementsService(mainList, get())
    }
}

private val modulesList = listOf(services, binds, repositories, ktor)

fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(modulesList)
    }
}
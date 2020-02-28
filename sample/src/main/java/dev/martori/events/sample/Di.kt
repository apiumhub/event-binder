package dev.martori.events.sample

import android.app.Application
import dev.martori.events.sample.binding.binds.bindAnimeList
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.data.network.ktor.KtorAnimeApi
import dev.martori.events.sample.data.repositories.AnimeStoreRepository
import dev.martori.events.sample.domain.repositories.AnimeRepository
import dev.martori.events.sample.domain.services.AndroidErrorLogger
import dev.martori.events.sample.domain.services.StoreAnimeListService
import dev.martori.events.sample.ui.AnimeList
import dev.martori.events.sample.ui.koinBind
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.host
import io.ktor.http.ContentType
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

private val services = module {
    singleBy<AnimeListService, StoreAnimeListService>()
    singleBy<ErrorLogger, AndroidErrorLogger>()
}

private val repositories = module {
    singleBy<AnimeRepository, AnimeStoreRepository>()
}

private val ktor = module {
    single {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
                acceptContentTypes = listOf(ContentType.parse("application/vnd.api+json"))
            }
            defaultRequest {
                host = "kitsu.io/api/edge"
            }
        }
    }

    single<AnimeApi> { KtorAnimeApi(get()) }
}

private val binds = module {
    koinBind<AnimeList> { animeList ->
        animeList.bindAnimeList(animeList, get())
    }
}

val modulesList = listOf(services, binds, ktor, repositories)

fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(modulesList)
    }
}
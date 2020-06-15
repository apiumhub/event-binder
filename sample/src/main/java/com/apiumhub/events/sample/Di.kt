package com.apiumhub.events.sample

import android.app.Application
import com.apiumhub.events.sample.binding.binds.bindAnimeDetails
import com.apiumhub.events.sample.binding.binds.bindAnimeList
import com.apiumhub.events.sample.binding.services.AnimeDetailsService
import com.apiumhub.events.sample.binding.services.AnimeListService
import com.apiumhub.events.sample.binding.services.ErrorLogger
import com.apiumhub.events.sample.data.network.api.AnimeApi
import com.apiumhub.events.sample.data.network.ktor.KtorAnimeApi
import com.apiumhub.events.sample.data.repositories.AnimeStoreRepository
import com.apiumhub.events.sample.domain.repositories.AnimeRepository
import com.apiumhub.events.sample.domain.services.AndroidErrorLogger
import com.apiumhub.events.sample.domain.services.NetworkAnimeDetailsService
import com.apiumhub.events.sample.domain.services.NetworkAnimeListService
import com.apiumhub.events.sample.ui.AnimeDetailsFragment
import com.apiumhub.events.sample.ui.AnimeListFragment
import com.apiumhub.events.sample.ui.koinBind
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.ANDROID
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.host
import io.ktor.http.ContentType
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

private val services = module {
    singleBy<AnimeListService, NetworkAnimeListService>()
    singleBy<AnimeDetailsService, NetworkAnimeDetailsService>()
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
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }
    }

    single<AnimeApi> { KtorAnimeApi(get()) }
}

private val binds = module {
    koinBind<AnimeListFragment> { animeList ->
        animeList.bindAnimeList(animeList, get())
    }
    koinBind<AnimeDetailsFragment> { animeDetails ->
        animeDetails.bindAnimeDetails(animeDetails, get())
    }

}

val modulesList = services + binds + ktor + repositories

fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(modulesList)
    }
}
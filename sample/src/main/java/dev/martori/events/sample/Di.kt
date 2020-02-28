package dev.martori.events.sample

import android.app.Application
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import dev.martori.events.sample.binding.binds.bindAnimeList
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.data.dto.toDomain
import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.data.network.ktor.KtorAnimeApi
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.Repository
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
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val services = module {
    single<AnimeListService> { StoreAnimeListService(get(named<Anime>())) }
    single<ErrorLogger> { AndroidErrorLogger() }
}

private fun <K : Any, O : Any> Store<K, O>.repo() = object : Repository<K, O> {
    override suspend fun get(key: K): O = this@repo.get(key)
}

private val stores = module {
    single(named<Anime>()) { StoreBuilder.fromNonFlow { request: AnimeRequest -> get<AnimeApi>().getAnimeList(request.count).map { it.toDomain() } }.build().repo() }
}

private val ktor = module {
    single() {
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

private val modulesList = listOf(services, binds, ktor, stores)

fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(modulesList)
    }
}
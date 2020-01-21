package dev.martori.events.sample

import android.app.Application
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import dev.martori.events.sample.binding.binds.bindAnimeList
import dev.martori.events.sample.binding.binds.bindDetailsNavigation
import dev.martori.events.sample.binding.binds.bindDetailsService
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.binding.services.*
import dev.martori.events.sample.data.dto.toDomain
import dev.martori.events.sample.data.inmemory.InMemoryCounterRepository
import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.data.network.api.DetailsApi
import dev.martori.events.sample.data.network.api.DetailsDto
import dev.martori.events.sample.data.network.api.toDomain
import dev.martori.events.sample.data.network.ktor.KtorAnimeApi
import dev.martori.events.sample.data.network.ktor.KtorDetailsApi
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.CounterRepository
import dev.martori.events.sample.domain.repositories.Repository
import dev.martori.events.sample.domain.services.*
import dev.martori.events.sample.ui.AnimeList
import dev.martori.events.sample.ui.DetailsFragment
import dev.martori.events.sample.ui.koinBind
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import io.ktor.http.ContentType
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val services = module {
    single<MainService> { MainDelayService() }
    single<CounterService> { DelayedCounterService(get()) }
    single<DetailsService> { StoreDetailsService(get(named<DetailsDto>())) }
    single<AnimeListService> { StoreAnimeListService(get(named<Anime>())) }
    single<LoadElementsService> { MockLoadElementsService() }
    single<ErrorLogger> { AndroidErrorLogger() }
}

private fun <K : Any, O : Any> Store<K, O>.repo() = object : Repository<K, O> {
    override suspend fun get(key: K): O = this@repo.get(key)
}

private val stores = module {
    single(named<DetailsDto>()) { StoreBuilder.fromNonFlow { id: Int -> get<DetailsApi>().getDetails(id).toDomain() }.build() }
    single(named<Anime>()) { StoreBuilder.fromNonFlow { request: AnimeRequest -> get<AnimeApi>().getAnimeList(request.count).map { it.toDomain() } }.build().repo() }
}

private val repositories = module {
    single<CounterRepository> { InMemoryCounterRepository() }
}

private val ktor = module {
    single(named("mock")) {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                host = "127.0.0.1:8080"
            }
        }
    }
    single(named("kitsu")) {
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
    single<DetailsApi> { KtorDetailsApi(get(named("mock"))) }
    single<AnimeApi> { KtorAnimeApi(get(named("kitsu"))) }
}

private val binds = module {
    koinBind<DetailsFragment> { details ->
        details.bindDetailsService(details, get())
        details.bindDetailsNavigation(details, get())
    }
    koinBind<AnimeList> { animeList ->
        animeList.bindAnimeList(animeList, get())
    }
}

private val modulesList = listOf(services, binds, repositories, ktor, stores)

fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(modulesList)
    }
}
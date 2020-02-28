package dev.martori.events.sample.data.repositories

import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.data.dto.toDomain
import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.AnimeRepository

class AnimeStoreRepository(api: AnimeApi) : AnimeRepository {
    private val listStore = StoreBuilder.fromNonFlow { request: AnimeListRequest -> api.getAnimeList(request.count).map { it.toDomain() } }.build()
    private val store = StoreBuilder.fromNonFlow { request: AnimeRequest -> api.getAnime(request.id).toDomain() }.build()

    override suspend fun getList(request: AnimeListRequest): List<Anime> = listStore.get(request)
    override suspend fun get(request: AnimeRequest): Anime = store.get(request)
}
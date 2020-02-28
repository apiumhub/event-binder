package dev.martori.events.sample.data.repositories

import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.data.dto.toDomain
import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.AnimeRepository

class AnimeStoreRepository(api: AnimeApi) : AnimeRepository {
    private val store = StoreBuilder.fromNonFlow { request: AnimeListRequest -> api.getAnimeList(request.count).map { it.toDomain() } }.build()

    override suspend fun getList(request: AnimeListRequest): List<Anime> = store.get(request)
}
package com.apiumhub.events.sample.data.repositories

import com.apiumhub.events.sample.binding.models.AnimeListRequest
import com.apiumhub.events.sample.binding.models.AnimeRequest
import com.apiumhub.events.sample.data.dto.toDomain
import com.apiumhub.events.sample.data.network.api.AnimeApi
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.repositories.AnimeRepository
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.get

class AnimeStoreRepository(api: AnimeApi) : AnimeRepository {
    private val listStore = StoreBuilder.fromNonFlow { request: AnimeListRequest ->
        api.getAnimeList(request.count).map { it.toDomain() }
    }.build()
    private val store = StoreBuilder.fromNonFlow { request: AnimeRequest -> api.getAnime(request.requestId).toDomain() }.build()

    override suspend fun getList(request: AnimeListRequest): List<Anime> = listStore.get(request)
    override suspend fun get(request: AnimeRequest): Anime = store.get(request)
}
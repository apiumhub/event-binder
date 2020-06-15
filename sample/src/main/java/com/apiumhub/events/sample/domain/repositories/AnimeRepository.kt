package com.apiumhub.events.sample.domain.repositories

import com.apiumhub.events.sample.binding.models.AnimeListRequest
import com.apiumhub.events.sample.binding.models.AnimeRequest
import com.apiumhub.events.sample.domain.entities.Anime

interface AnimeRepository {
    suspend fun getList(request: AnimeListRequest): List<Anime>
    suspend fun get(request: AnimeRequest): Anime
}
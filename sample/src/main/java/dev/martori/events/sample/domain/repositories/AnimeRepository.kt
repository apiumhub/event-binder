package dev.martori.events.sample.domain.repositories

import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.domain.entities.Anime

interface AnimeRepository {
    suspend fun getList(request: AnimeListRequest): List<Anime>
    suspend fun get(request: AnimeRequest): Anime
}
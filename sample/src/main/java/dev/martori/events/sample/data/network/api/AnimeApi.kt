package dev.martori.events.sample.data.network.api

import dev.martori.events.sample.data.dto.AnimeDto

interface AnimeApi {
    suspend fun getAnimeList(offset: Int): List<AnimeDto>
    suspend fun getAnime(id: Int): AnimeDto
}
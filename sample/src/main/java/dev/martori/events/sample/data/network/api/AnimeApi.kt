package dev.martori.events.sample.data.network.api

import dev.martori.events.sample.data.dto.AnimeDto

interface AnimeApi {
    suspend fun getAnimeListByYear(year: Int, offset: Int): List<AnimeDto>
}
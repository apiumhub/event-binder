package com.apiumhub.events.sample.data.network.api

import com.apiumhub.events.sample.data.dto.AnimeDto

interface AnimeApi {
    suspend fun getAnimeList(offset: Int): List<AnimeDto>
    suspend fun getAnime(id: String): AnimeDto
}
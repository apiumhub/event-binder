package dev.martori.events.sample.data.network.ktor

import dev.martori.events.sample.data.network.api.AnimeApi
import dev.martori.events.sample.data.network.api.AnimeDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAnimeApi(private val client: HttpClient) : AnimeApi {
    override suspend fun getAnimeListByYear(year: Int): List<AnimeDto> = client.get("/anime/filter[seasonYear]=$year")
}
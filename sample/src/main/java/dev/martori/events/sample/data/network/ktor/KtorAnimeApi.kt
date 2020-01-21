package dev.martori.events.sample.data.network.ktor

import dev.martori.events.sample.data.dto.AnimeDto
import dev.martori.events.sample.data.network.api.AnimeApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAnimeApi(private val client: HttpClient) : AnimeApi {
    override suspend fun getAnimeList(offset: Int): List<AnimeDto> =
        client.get<Response<AnimeDto>>("/anime?page[offset]=$offset&sort=-favoritesCount").dtos()
}

data class Response<T>(val data: List<ResponseObject<T>>) {
    fun dtos() = data.map { it.attributes }
}

data class ResponseObject<T>(val attributes: T)
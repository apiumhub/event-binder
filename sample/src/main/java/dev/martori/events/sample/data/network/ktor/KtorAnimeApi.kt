package dev.martori.events.sample.data.network.ktor

import dev.martori.events.sample.data.dto.AnimeDto
import dev.martori.events.sample.data.network.api.AnimeApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAnimeApi(private val client: HttpClient) : AnimeApi {
    override suspend fun getAnimeList(offset: Int): List<AnimeDto> =
        client.get<Response<AnimeDto>>("/anime?page[offset]=$offset&sort=-favoritesCount")
            .dtos()
            .map { (id, dto) -> dto.copy(id = id) }

    override suspend fun getAnime(id: Int): AnimeDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

interface NetworkDto {
    val id: String
}

data class Response<T : NetworkDto>(val data: List<ResponseObject<T>>) {
    fun dtos() = data.map { it.id to it.attributes }
}

data class ResponseObject<T>(val id: String, val attributes: T)

data class AnimeImage(val small: String, val large: String)
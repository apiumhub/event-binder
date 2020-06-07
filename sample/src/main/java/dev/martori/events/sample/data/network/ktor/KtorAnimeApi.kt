package dev.martori.events.sample.data.network.ktor

import dev.martori.events.sample.data.dto.AnimeDto
import dev.martori.events.sample.data.network.api.AnimeApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorAnimeApi(private val client: HttpClient) : AnimeApi {
    override suspend fun getAnimeList(offset: Int): List<AnimeDto> =
        client.get<ListResponse<AnimeDto>>("/anime?page[offset]=$offset&sort=-favoritesCount").dtos

    override suspend fun getAnime(id: Int): AnimeDto = client.get<Response<AnimeDto>>("/anime/$id").dto
}

interface NetworkDto<T : NetworkDto<T>> {
    val id: String
    fun withId(id: String): T
}

data class ListResponse<T : NetworkDto<T>>(private val data: List<ResponseObject<T>>) {
    val dtos: List<T>
        get() = data.map { it.withId() }
}

data class Response<T : NetworkDto<T>>(private val data: ResponseObject<T>) {
    val dto: T
        get() = data.withId()
}

data class ResponseObject<T : NetworkDto<T>>(val id: String, val attributes: T) {
    fun withId() =
        attributes.withId(id)
}

data class AnimeImage(val small: String, val large: String)
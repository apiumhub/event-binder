package dev.martori.events.sample.data.network.ktor

import dev.martori.events.sample.data.network.api.DetailsApi
import dev.martori.events.sample.data.network.api.DetailsDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get


class KtorDetailsApi(private val client: HttpClient) : DetailsApi {
    override suspend fun getDetails(id: Int): DetailsDto = client.get("/details/$id")
}
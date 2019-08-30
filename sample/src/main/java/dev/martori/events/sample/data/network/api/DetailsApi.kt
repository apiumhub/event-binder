package dev.martori.events.sample.data.network.api

interface DetailsApi {
    suspend fun getDetails(id: Int): DetailsDto
}

data class DetailsDto(val id: Int, val name: String)

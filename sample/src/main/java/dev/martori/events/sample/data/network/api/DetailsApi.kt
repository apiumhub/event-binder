package dev.martori.events.sample.data.network.api

import dev.martori.events.sample.domain.entities.Details
import kotlinx.serialization.Serializable

interface DetailsApi {
    suspend fun getDetails(id: Int): DetailsDto
}

@Serializable
data class DetailsDto(val id: Int, val name: String)

fun DetailsDto.toDomain(): Details = Details(id, name)
package dev.martori.events.sample.data.dto

import dev.martori.events.sample.data.network.ktor.AnimeImage
import dev.martori.events.sample.data.network.ktor.NetworkDto
import dev.martori.events.sample.domain.entities.Anime

data class AnimeDto(override val id: String, val canonicalTitle: String, val posterImage: AnimeImage) : NetworkDto

fun AnimeDto.toDomain() = Anime(id, canonicalTitle, posterImage.small)
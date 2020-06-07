package dev.martori.events.sample.data.dto

import dev.martori.events.sample.data.network.ktor.AnimeImage
import dev.martori.events.sample.data.network.ktor.NetworkDto
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id
import dev.martori.events.sample.domain.entities.ImageUrl

data class AnimeDto(override val id: String, val canonicalTitle: String, val posterImage: AnimeImage, val coverImage: AnimeImage, val synopsis: String) : NetworkDto<AnimeDto> {
    override fun withId(newId: String) = copy(id = newId)
}

fun AnimeDto.toDomain() = Anime(Id(id), canonicalTitle, ImageUrl(posterImage.small), ImageUrl(coverImage.large), synopsis)
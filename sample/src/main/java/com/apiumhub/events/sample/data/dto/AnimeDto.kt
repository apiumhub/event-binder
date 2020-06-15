package com.apiumhub.events.sample.data.dto

import com.apiumhub.events.sample.data.network.ktor.AnimeImage
import com.apiumhub.events.sample.data.network.ktor.NetworkDto
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id
import com.apiumhub.events.sample.domain.entities.ImageUrl

data class AnimeDto(override val id: String, val canonicalTitle: String, val posterImage: AnimeImage, val coverImage: AnimeImage, val synopsis: String) : NetworkDto<AnimeDto> {
    override fun withId(id: String) = copy(id = id)
}

fun AnimeDto.toDomain() = Anime(Id(id), canonicalTitle, ImageUrl(posterImage.small), ImageUrl(coverImage.large), synopsis)
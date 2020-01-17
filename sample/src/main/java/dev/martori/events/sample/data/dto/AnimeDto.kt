package dev.martori.events.sample.data.dto

import dev.martori.events.sample.domain.entities.Anime

data class AnimeDto(val canonicalTitle: String)

fun AnimeDto.toDomain() = Anime(canonicalTitle)
package dev.martori.events.sample.ui.models

import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id
import dev.martori.events.sample.domain.entities.ImageUrl

data class AnimeViewModel(val id: Id, val name: String, val imageUrl: ImageUrl)

fun Anime.toViewModel() = AnimeViewModel(id, name, posterImage)
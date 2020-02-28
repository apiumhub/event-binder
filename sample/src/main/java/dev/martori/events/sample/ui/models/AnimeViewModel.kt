package dev.martori.events.sample.ui.models

import dev.martori.events.sample.domain.entities.Anime

data class AnimeViewModel(val id: String, val name: String, val imageUrl: String)

fun Anime.toViewModel() = AnimeViewModel(id, name, posterImage)
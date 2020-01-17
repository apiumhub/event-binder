package dev.martori.events.sample.ui.models

import dev.martori.events.sample.domain.entities.Anime

data class AnimeViewModel(val name: String)

fun Anime.toViewModel() = AnimeViewModel(name)
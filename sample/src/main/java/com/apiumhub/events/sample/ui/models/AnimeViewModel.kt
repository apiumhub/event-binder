package com.apiumhub.events.sample.ui.models

import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id
import com.apiumhub.events.sample.domain.entities.ImageUrl

data class AnimeViewModel(val id: Id, val name: String, val imageUrl: ImageUrl)

fun Anime.toViewModel() = AnimeViewModel(id, name, posterImage)
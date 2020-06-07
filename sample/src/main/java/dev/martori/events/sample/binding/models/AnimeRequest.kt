package dev.martori.events.sample.binding.models

import dev.martori.events.sample.domain.entities.Id

data class AnimeRequest(private val id: Id) {
    val requestId: String = id.id
}
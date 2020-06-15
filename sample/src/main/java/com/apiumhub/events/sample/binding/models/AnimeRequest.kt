package com.apiumhub.events.sample.binding.models

import com.apiumhub.events.sample.domain.entities.Id

data class AnimeRequest(private val id: Id) {
    val requestId: String = id.id
}
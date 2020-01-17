package dev.martori.events.sample.ui.models

import dev.martori.events.sample.domain.entities.Details

data class DetailViewModel(val id: Int, val name: String)

fun Details.toViewModel() = DetailViewModel(id, name)
package dev.martori.events.sample.domain.entities

import java.io.Serializable

inline class ImageUrl(val uri: String)

data class Id(val id: String) : Serializable

data class Anime(val id: Id, val name: String, val posterImage: ImageUrl, val coverImage: ImageUrl, val synopsis: String)
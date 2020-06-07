package dev.martori.events.sample.domain.entities

inline class ImageUrl(val uri: String)

inline class Id(val id: String)

data class Anime(val id: Id, val name: String, val posterImage: ImageUrl, val coverImage: ImageUrl, val synopsis: String)
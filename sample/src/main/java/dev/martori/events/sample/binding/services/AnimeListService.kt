package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.core.Receiver
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.domain.entities.Anime

interface AnimeListService : Bindable {
    val loadAnime: Receiver<AnimeListRequest>
    val errorReceived: Event<Error>
    val startFetching: EventU
    val animeListReceived: Event<List<Anime>>
}
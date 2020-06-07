package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.core.Receiver
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id

interface AnimeDetailsService : Bindable {
    val loadAnime: Receiver<Id>
    val errorReceived: Event<Error>
    val startedFetching: EventU
    val animeReceived: Event<Anime>
}
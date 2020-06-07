package dev.martori.events.sample.binding.views

import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.core.Receiver
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id

interface AnimeDetailsView {
    val requestAnimeDetails: Event<Id>
    val displayAnime: Receiver<Anime>
    val goBack: EventU
}
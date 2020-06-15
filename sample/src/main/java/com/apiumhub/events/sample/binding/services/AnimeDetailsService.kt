package com.apiumhub.events.sample.binding.services

import com.apiumhub.events.core.Bindable
import com.apiumhub.events.core.Event
import com.apiumhub.events.core.EventU
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id

interface AnimeDetailsService : Bindable {
    val loadAnime: Receiver<Id>
    val errorReceived: Event<Error>
    val startedFetching: EventU
    val animeReceived: Event<Anime>
}
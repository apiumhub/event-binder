package com.apiumhub.events.sample.binding.services

import com.apiumhub.events.core.Bindable
import com.apiumhub.events.core.Event
import com.apiumhub.events.core.EventU
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.sample.binding.models.AnimeListRequest
import com.apiumhub.events.sample.domain.entities.Anime

interface AnimeListService : Bindable {
    val loadAnime: Receiver<AnimeListRequest>
    val errorReceived: Event<Error>
    val startedFetching: EventU
    val animeListReceived: Event<List<Anime>>
}
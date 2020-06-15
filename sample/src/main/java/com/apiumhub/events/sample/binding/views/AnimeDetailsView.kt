package com.apiumhub.events.sample.binding.views

import com.apiumhub.events.core.Event
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.core.ReceiverU
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id

interface AnimeDetailsView {
    val requestAnimeDetails: Event<Id>
    val displayAnime: Receiver<Anime>
    val displayError: Receiver<Error>
    val displayLoading: ReceiverU
}
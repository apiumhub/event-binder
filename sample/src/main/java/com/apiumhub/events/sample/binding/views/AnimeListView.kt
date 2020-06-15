package com.apiumhub.events.sample.binding.views

import com.apiumhub.events.core.Event
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.core.ReceiverU
import com.apiumhub.events.sample.binding.models.AnimeListRequest
import com.apiumhub.events.sample.domain.entities.Anime

interface AnimeListView {
    val requestAnime: Event<AnimeListRequest>
    val onError: Receiver<Error>
    val onLoading: ReceiverU
    val displayAnimeList: Receiver<List<Anime>>
}
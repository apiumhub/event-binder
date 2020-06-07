package dev.martori.events.sample.binding.views

import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.domain.entities.Anime

interface AnimeListView {
    val requestAnime: Event<AnimeListRequest>
    val onError: Receiver<Error>
    val onLoading: ReceiverU
    val displayAnimeList: Receiver<List<Anime>>
}
package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.AnimeDetailsService
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.views.AnimeDetailsView
import dev.martori.events.sample.binding.views.AnimeListView

fun ViewBindable.bindAnimeList(view: AnimeListView, service: AnimeListService) = bind {
    view.displayAnimeList via service.animeListReceived
    view.onError via service.errorReceived
    view.onLoading via service.startedFetching
    view.requestAnime via service.loadAnime
}

fun ViewBindable.bindAnimeDetails(view: AnimeDetailsView, service: AnimeDetailsService) = bind {
    view.requestAnimeDetails via service.loadAnime
    view.displayError via service.errorReceived
    view.displayLoading via service.startedFetching
    view.displayAnime via service.animeReceived
}
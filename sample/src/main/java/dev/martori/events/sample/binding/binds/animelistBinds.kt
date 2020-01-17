package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.views.AnimeListView

fun ViewBindable.bindAnimeList(view: AnimeListView, service: AnimeListService) = bind {
    view.displayAnimeList via service.animeListReceived
    view.onError via service.error
    view.onLoading via service.startFetching
    view.requestAnimeByYear via service.loadAnimeByYear
}
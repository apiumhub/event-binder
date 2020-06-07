package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.services.Navigator
import dev.martori.events.sample.binding.views.AnimeDetailsView
import dev.martori.events.sample.binding.views.AnimeListView

fun ViewBindable.bindAnimeList(view: AnimeListView, service: AnimeListService, navigator: Navigator) = bind {
    view.displayAnimeList via service.animeListReceived
    view.onError via service.errorReceived
    view.onLoading via service.startFetching
    view.requestAnime via service.loadAnime
    view.openAnimeDetails via navigator.openDetails
}

fun ViewBindable.bindAnimeDetails(view: AnimeDetailsView, navigator: Navigator) = bind {
    view.goBack via navigator.openList
}
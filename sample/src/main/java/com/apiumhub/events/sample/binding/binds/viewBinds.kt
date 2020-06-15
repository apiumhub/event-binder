package com.apiumhub.events.sample.binding.binds

import com.apiumhub.events.android.ViewBindable
import com.apiumhub.events.android.bind
import com.apiumhub.events.sample.binding.services.AnimeDetailsService
import com.apiumhub.events.sample.binding.services.AnimeListService
import com.apiumhub.events.sample.binding.views.AnimeDetailsView
import com.apiumhub.events.sample.binding.views.AnimeListView

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
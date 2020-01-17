package dev.martori.events.sample.binding.binds

import dev.martori.events.core.Bindable
import dev.martori.events.core.bind
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.binding.views.AsyncView

fun Bindable.bindDetailsErrors(detailsService: DetailsService, errorLogger: ErrorLogger) = bind {
    detailsService.sendState
        .filter { it is AsyncView.Error }
        .map { it as AsyncView.Error<*> } via errorLogger.onAsyncError
}

fun Bindable.bindAnimeListErrors(service: AnimeListService, errorLogger: ErrorLogger) = bind {
    service.error via errorLogger.onError
}
package dev.martori.events.sample.binding.binds

import dev.martori.events.core.Bindable
import dev.martori.events.core.bind
import dev.martori.events.sample.binding.services.AnimeDetailsService
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.binding.services.ErrorLogger

fun Bindable.bindAnimeListErrors(service: AnimeListService, errorLogger: ErrorLogger) = bind {
    service.errorReceived via errorLogger.onError
}

fun Bindable.bindAnimeDetailsErrors(service: AnimeDetailsService, errorLogger: ErrorLogger) = bind {
    service.errorReceived via errorLogger.onError
}
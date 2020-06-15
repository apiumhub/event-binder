package com.apiumhub.events.sample.binding.binds

import com.apiumhub.events.core.Bindable
import com.apiumhub.events.core.bind
import com.apiumhub.events.sample.binding.services.AnimeDetailsService
import com.apiumhub.events.sample.binding.services.AnimeListService
import com.apiumhub.events.sample.binding.services.ErrorLogger

fun Bindable.bindAnimeListErrors(service: AnimeListService, errorLogger: ErrorLogger) = bind {
    service.errorReceived via errorLogger.onError
}

fun Bindable.bindAnimeDetailsErrors(service: AnimeDetailsService, errorLogger: ErrorLogger) = bind {
    service.errorReceived via errorLogger.onError
}
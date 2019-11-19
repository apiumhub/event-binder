package dev.martori.events.sample.binding.binds

import dev.martori.events.core.Bindable
import dev.martori.events.core.bind
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.binding.views.DetailViewModel

fun Bindable.bindDetailsErrors(detailsService: DetailsService, errorLogger: ErrorLogger<DetailViewModel>) = bind {
    detailsService.sendState via errorLogger.onError
}
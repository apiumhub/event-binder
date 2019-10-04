package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.LoadElementsService
import dev.martori.events.sample.binding.views.MainListView

fun ViewBindable.bindLoadElementsService(view: MainListView, service: LoadElementsService) = bind {
    view.requestListElements via service.startLoading
    service.elementsLoaded via view.showListElements
}
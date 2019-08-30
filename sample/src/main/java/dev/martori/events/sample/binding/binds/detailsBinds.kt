package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.DetailView

fun ViewBindable.bindDetailsService(view: DetailView, service: DetailsService) = bind {
    view.renderError via service.error
    view.renderLoading via service.startProcess
    view.renderModel via service.modelLoaded
    view.loadDetails via service.loadDetails
}
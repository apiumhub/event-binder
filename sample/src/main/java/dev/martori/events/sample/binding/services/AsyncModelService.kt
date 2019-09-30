package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Event
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailViewModel

interface AsyncModelService<T> : Bindable {
    val sendState: Event<AsyncView<DetailViewModel>>
}
package dev.martori.events.sample.binding.views

import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU

interface AsyncModelView<T> {
    val renderLoading: InEventU
    val renderModel: InEvent<T>
    val renderError: InEvent<Error>
}
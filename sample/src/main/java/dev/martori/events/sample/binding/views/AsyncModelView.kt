package dev.martori.events.sample.binding.views

import dev.martori.events.core.Receiver

interface AsyncModelView<T> {
    val renderState: Receiver<AsyncView<T>>
}

sealed class AsyncView<T> {
    class Init<T> : AsyncView<T>()
    class Loading<T> : AsyncView<T>()
    data class Success<T>(val model: T) : AsyncView<T>()
    data class Error<T>(val reason: kotlin.Error) : AsyncView<T>()
}
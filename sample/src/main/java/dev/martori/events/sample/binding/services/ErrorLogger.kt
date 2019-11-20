package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver
import dev.martori.events.sample.binding.views.AsyncView

interface ErrorLogger : Bindable {
    val onError: Receiver<AsyncView.Error<*>>
}
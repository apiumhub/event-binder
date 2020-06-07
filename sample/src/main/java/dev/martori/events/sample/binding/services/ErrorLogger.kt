package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver

interface ErrorLogger : Bindable {
    val onError: Receiver<Error>
}
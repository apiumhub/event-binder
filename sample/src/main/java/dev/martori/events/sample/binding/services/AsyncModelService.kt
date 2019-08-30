package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.OutEvent
import dev.martori.events.core.OutEventU

interface AsyncModelService<T> : Bindable {
    val modelLoaded: OutEvent<T>
    val error: OutEvent<Error>
    val startProcess: OutEventU
}
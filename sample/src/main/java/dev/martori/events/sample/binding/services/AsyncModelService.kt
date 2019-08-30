package dev.martori.events.sample.binding.services

import dev.martori.events.core.OutEvent
import dev.martori.events.core.OutEventU

interface AsyncModelService<T> {
    val modelLoaded: OutEvent<T>
    val error: OutEvent<Error>
    val startProcess: OutEventU
}
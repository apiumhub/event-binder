package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver
import dev.martori.events.core.Event
import dev.martori.events.sample.domain.entities.ListElement

interface LoadElementsService : Bindable {
    val elementsLoaded: Event<List<ListElement>>
    val startLoading: Receiver<Int>
}
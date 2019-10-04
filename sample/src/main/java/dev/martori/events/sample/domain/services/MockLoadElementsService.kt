package dev.martori.events.sample.domain.services

import dev.martori.events.core.Event
import dev.martori.events.core.consumer
import dev.martori.events.core.event
import dev.martori.events.sample.binding.services.LoadElementsService
import dev.martori.events.sample.domain.entities.ListElement

class MockLoadElementsService : LoadElementsService {
    override val elementsLoaded: Event<List<ListElement>> = event()
    override val startLoading = consumer<Int> {
        elementsLoaded(listOf(ListElement(1, "1")))
    }
}
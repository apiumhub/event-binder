package dev.martori.events.sample.domain.services

import dev.martori.events.core.Event
import dev.martori.events.core.consumer
import dev.martori.events.core.event
import dev.martori.events.sample.binding.services.LoadElementsService
import dev.martori.events.sample.domain.entities.ListElement

class MockLoadElementsService : LoadElementsService {
    override val elementsLoaded: Event<List<ListElement>> = event()
    override val startLoading = consumer<Int> {
        elementsLoaded(
            listOf(
                ListElement(1, "1"),
                ListElement(2, "2"),
                ListElement(3, "3"),
                ListElement(5, "5"),
                ListElement(6, "6"),
                ListElement(7, "7"),
                ListElement(8, "8"),
                ListElement(9, "9"),
                ListElement(10, "10"),
                ListElement(11, "11"),
                ListElement(12, "12"),
                ListElement(13, "13"),
                ListElement(13, "13"),
                ListElement(14, "14"),
                ListElement(15, "15"),
                ListElement(16, "16"),
                ListElement(17, "17"),
                ListElement(18, "18"),
                ListElement(19, "19")
            )
        )
    }
}
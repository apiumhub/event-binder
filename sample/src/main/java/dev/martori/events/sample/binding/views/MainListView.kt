package dev.martori.events.sample.binding.views

import dev.martori.events.core.Receiver
import dev.martori.events.core.Event
import dev.martori.events.sample.domain.entities.ListElement

interface MainListView {
    val openDetails: Event<Int>
    val requestListElements: Event<Int>
    val showListElements: Receiver<List<ListElement>>
}
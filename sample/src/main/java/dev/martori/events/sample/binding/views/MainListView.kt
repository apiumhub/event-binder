package dev.martori.events.sample.binding.views

import dev.martori.events.core.Event

interface MainListView {
    val openDetails: Event<Int>
}
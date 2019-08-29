package dev.martori.events.sample.binding.views

import dev.martori.events.core.OutEvent

interface MainListView {
    val openDetails: OutEvent<Int>
}
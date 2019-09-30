package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Consumer
import dev.martori.events.core.ConsumerU

interface Navigator : Bindable {
    val openDetails: Consumer<Int>
    val openList: ConsumerU
}
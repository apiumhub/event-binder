package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU

interface Navigator : Bindable {
    val openDetails: InEvent<Int>
    val openList: InEventU
}
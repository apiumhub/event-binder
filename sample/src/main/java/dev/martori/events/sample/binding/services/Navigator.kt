package dev.martori.events.sample.binding.services

import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU

interface Navigator {
    val openDetails: InEvent<Int>
    val openList: InEventU
}
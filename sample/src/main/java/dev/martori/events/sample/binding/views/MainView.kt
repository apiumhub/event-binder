package dev.martori.events.sample.binding.views

import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent


interface MainView {
    val clickButton: OutEvent<Int>
    val showText: InEvent<String>
    val showCounter: InEvent<Int>
}
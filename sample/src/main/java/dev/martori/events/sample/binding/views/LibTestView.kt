package dev.martori.events.sample.binding.views

import dev.martori.events.core.Consumer
import dev.martori.events.core.Event


interface LibTestView {
    val clickButton: Event<Int>
    val showText: Consumer<String>
    val showCounter: Consumer<Int>
}
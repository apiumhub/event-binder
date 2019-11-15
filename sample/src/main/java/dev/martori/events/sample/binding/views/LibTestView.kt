package dev.martori.events.sample.binding.views

import dev.martori.events.core.Receiver
import dev.martori.events.core.Event


interface LibTestView {
    val clickButton: Event<Int>
    val showText: Receiver<String>
    val showCounter: Receiver<Int>
}
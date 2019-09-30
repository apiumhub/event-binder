package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Consumer
import dev.martori.events.core.Event

interface MainService : Bindable {
    val requestTextById: Consumer<Int>
    val sendText: Event<String>
}
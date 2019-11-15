package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver
import dev.martori.events.core.Event

interface MainService : Bindable {
    val requestTextById: Receiver<Int>
    val sendText: Event<String>
}
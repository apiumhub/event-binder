package dev.martori.events.sample.binding

import dev.martori.events.core.Bindable
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent

interface MainService : Bindable {
    val requestTextById: InEvent<Int>
    val sendText: OutEvent<String>
}
package dev.martori.kataeventarch.binding

import dev.martori.eventarch.Bindable
import dev.martori.eventarch.InEvent
import dev.martori.eventarch.OutEvent

interface MainService : Bindable {
    val requestTextById: InEvent<Int>
    val sendText: OutEvent<String>
}
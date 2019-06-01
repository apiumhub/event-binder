package dev.martori.kataeventarch.binding

import cat.martori.eventarch.Bindable
import cat.martori.eventarch.InEvent
import cat.martori.eventarch.OutEvent

interface MainService : Bindable {
    val requestTextById: InEvent<Int>
    val sendText: OutEvent<String>
}
package dev.martori.kataeventarch.binding

import cat.martori.core.Bindable
import cat.martori.core.InEvent
import cat.martori.core.OutEvent

interface MainService : Bindable {
    val requestTextById: InEvent<Int>
    val sendText: OutEvent<String>
}
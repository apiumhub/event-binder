package dev.martori.kataeventarch.binding

import dev.martori.eventarch.Bindable
import dev.martori.eventarch.InEventU
import dev.martori.eventarch.OutEvent

interface CounterService : Bindable {
    val totalCount: OutEvent<Int>
    val modifyCounter: InEventU
}
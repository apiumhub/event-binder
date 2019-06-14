package dev.martori.kataeventarch.binding

import cat.martori.eventarch.Bindable
import cat.martori.eventarch.InEventU
import cat.martori.eventarch.OutEvent

interface CounterService : Bindable {
    val totalCount: OutEvent<Int>
    val modifyCounter: InEventU
}
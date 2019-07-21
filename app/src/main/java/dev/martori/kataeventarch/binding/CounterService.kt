package dev.martori.kataeventarch.binding

import cat.martori.core.Bindable
import cat.martori.core.InEventU
import cat.martori.core.OutEvent

interface CounterService : Bindable {
    val totalCount: OutEvent<Int>
    val modifyCounter: InEventU
}
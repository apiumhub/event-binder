package dev.martori.events.sample.binding

import dev.martori.events.core.Bindable
import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent


interface CounterService : Bindable {
    val totalCount: OutEvent<Int>
    val modifyCounter: InEventU
}
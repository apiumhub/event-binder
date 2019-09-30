package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.ConsumerU
import dev.martori.events.core.Event


interface CounterService : Bindable {
    val totalCount: Event<Int>
    val modifyCounter: ConsumerU
}
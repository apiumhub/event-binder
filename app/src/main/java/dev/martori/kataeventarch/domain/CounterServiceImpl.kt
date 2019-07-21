package dev.martori.kataeventarch.domain

import cat.martori.core.*
import dev.martori.kataeventarch.binding.CounterService

class CounterServiceImpl : CounterService {
    fun modifyCounterTest() {
        this.modifyCounter.dispatch()
    }

    private var count = 0

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = inEvent {
        totalCount(++count)
    }

}
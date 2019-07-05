package dev.martori.kataeventarch.domain

import dev.martori.eventarch.InEventU
import dev.martori.eventarch.OutEvent
import dev.martori.eventarch.inEvent
import dev.martori.eventarch.outEvent
import dev.martori.kataeventarch.binding.CounterService

class CounterServiceImpl : CounterService {

    private var count = 0

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = inEvent {
        totalCount(++count)
    }

}
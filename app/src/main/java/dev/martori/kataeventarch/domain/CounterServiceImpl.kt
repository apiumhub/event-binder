package dev.martori.kataeventarch.domain

import cat.martori.eventarch.InEventU
import cat.martori.eventarch.OutEvent
import cat.martori.eventarch.inEvent
import cat.martori.eventarch.outEvent
import dev.martori.kataeventarch.binding.CounterService

class CounterServiceImpl : CounterService {

    private var count = 0

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = inEvent {
        totalCount(++count)
    }

}
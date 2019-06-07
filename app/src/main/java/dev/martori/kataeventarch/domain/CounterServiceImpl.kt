package dev.martori.kataeventarch.domain

import cat.martori.eventarch.InEvent
import cat.martori.eventarch.OutEvent
import cat.martori.eventarch.inEvent
import cat.martori.eventarch.outEvent
import dev.martori.kataeventarch.binding.CounterService
import dev.martori.kataeventarch.ui.MainActivity.Companion.LEFT
import dev.martori.kataeventarch.ui.MainActivity.Companion.RIGHT

class CounterServiceImpl : CounterService {

    private var count = 0

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEvent<Int> = inEvent {
        when (it) {
            LEFT -> count--
            RIGHT -> count++
            else -> {
            }
        }
        totalCount(count)
    }

}
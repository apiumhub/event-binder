package dev.martori.kataeventarch.domain

import cat.martori.core.InEventU
import cat.martori.core.OutEvent
import cat.martori.core.inEvent
import cat.martori.core.outEvent
import dev.martori.kataeventarch.binding.CounterService
import kotlinx.coroutines.delay

class CounterServiceImpl : CounterService {
    private val repository = Repository()

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = inEvent {
        totalCount(repository.getNewCount())
    }

}

class Repository {
    private var counter = 0
    suspend fun getNewCount(): Int {
        delay(200)
        return ++counter
    }
}
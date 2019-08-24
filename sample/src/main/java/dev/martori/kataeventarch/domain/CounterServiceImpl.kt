package dev.martori.kataeventarch.domain

import cat.martori.core.InEventU
import cat.martori.core.OutEvent
import cat.martori.core.inEvent
import cat.martori.core.outEvent
import dev.martori.kataeventarch.binding.CounterService
import kotlinx.coroutines.delay

class CounterServiceImpl(private val repository: CounterRepository = InMemoryCounterRepository()) :
    CounterService {

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = inEvent {
        totalCount(repository.getNewCount())
    }

}

interface CounterRepository {
    suspend fun getNewCount(): Int
}

class InMemoryCounterRepository : CounterRepository {
    private var counter = 0
    override suspend fun getNewCount(): Int {
        delay(200)
        return ++counter
    }
}
package dev.martori.events.sample.domain

import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.core.inEvent
import dev.martori.events.core.outEvent
import dev.martori.events.sample.binding.CounterService
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
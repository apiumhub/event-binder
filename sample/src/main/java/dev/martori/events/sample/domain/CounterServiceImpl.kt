package dev.martori.events.sample.domain

import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.core.coInEvent
import dev.martori.events.core.outEvent
import dev.martori.events.sample.binding.CounterService
import kotlinx.coroutines.delay

class CounterServiceImpl(private val repository: CounterRepository = InMemoryCounterRepository()) : CounterService {

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = coInEvent {
        println(coroutineContext)
        //this delay actually breaks the test because it is using the default dispatcher, we need a way to pass the dispatcher through the test
        delay(1000)
        totalCount(repository.getNewCount())
    }

}

interface CounterRepository {
    suspend fun getNewCount(): Int
}

class InMemoryCounterRepository : CounterRepository {
    private var counter = 0
    override suspend fun getNewCount(): Int {
        delay(1000)
        return ++counter
    }
}
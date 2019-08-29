package dev.martori.events.sample.domain.services

import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.core.coInEvent
import dev.martori.events.core.outEvent
import dev.martori.events.sample.binding.services.CounterService
import dev.martori.events.sample.data.inmemory.InMemoryCounterRepository
import dev.martori.events.sample.domain.repositories.CounterRepository
import kotlinx.coroutines.delay

class DelayedCounterService(private val repository: CounterRepository = InMemoryCounterRepository()) : CounterService {

    override val totalCount: OutEvent<Int> = outEvent()
    override val modifyCounter: InEventU = coInEvent {
        delay(1000)
        totalCount(repository.getNewCount())
    }

}
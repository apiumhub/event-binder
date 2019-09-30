package dev.martori.events.sample.domain.services

import dev.martori.events.core.ConsumerU
import dev.martori.events.core.Event
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendConsumer
import dev.martori.events.sample.binding.services.CounterService
import dev.martori.events.sample.domain.repositories.CounterRepository
import kotlinx.coroutines.delay

class DelayedCounterService(private val repository: CounterRepository) : CounterService {

    override val totalCount: Event<Int> = event()
    override val modifyCounter: ConsumerU = suspendConsumer {
        delay(1000)
        totalCount(repository.getNewCount())
    }

}
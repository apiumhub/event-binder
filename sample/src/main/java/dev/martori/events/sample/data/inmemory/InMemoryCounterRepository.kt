package dev.martori.events.sample.data.inmemory

import dev.martori.events.sample.domain.repositories.CounterRepository
import kotlinx.coroutines.delay

class InMemoryCounterRepository : CounterRepository {
    private var counter = 0
    override suspend fun getNewCount(): Int {
        delay(3000)
        return ++counter
    }
}
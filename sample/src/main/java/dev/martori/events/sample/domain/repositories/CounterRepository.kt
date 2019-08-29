package dev.martori.events.sample.domain.repositories

interface CounterRepository {
    suspend fun getNewCount(): Int
}
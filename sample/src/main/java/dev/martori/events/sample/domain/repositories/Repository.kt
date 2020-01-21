package dev.martori.events.sample.domain.repositories

interface Repository<Key, Value> {
    suspend fun get(key: Key): Value
}
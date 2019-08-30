package dev.martori.events.sample.binding.services

import dev.martori.events.core.InEvent

interface DetailsService {
    val loadDetails: InEvent<Int>
}
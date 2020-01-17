package dev.martori.events.sample.binding.services

import dev.martori.events.core.Receiver
import dev.martori.events.sample.domain.entities.Details

interface DetailsService : AsyncModelService<Details> {
    val loadDetails: Receiver<Int>
}
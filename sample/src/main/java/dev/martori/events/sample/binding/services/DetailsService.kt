package dev.martori.events.sample.binding.services

import dev.martori.events.core.Receiver
import dev.martori.events.sample.binding.views.DetailViewModel

interface DetailsService : AsyncModelService<DetailViewModel> {
    val loadDetails: Receiver<Int>
}
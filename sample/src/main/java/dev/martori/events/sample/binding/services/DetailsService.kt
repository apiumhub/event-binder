package dev.martori.events.sample.binding.services

import dev.martori.events.core.Consumer
import dev.martori.events.sample.binding.views.DetailViewModel

interface DetailsService : AsyncModelService<DetailViewModel> {
    val loadDetails: Consumer<Int>
}
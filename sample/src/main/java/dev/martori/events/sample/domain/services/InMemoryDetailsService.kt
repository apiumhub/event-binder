package dev.martori.events.sample.domain.services

import dev.martori.events.core.*
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.DetailViewModel
import kotlinx.coroutines.delay

class InMemoryDetailsService : DetailsService {

    override val modelLoaded: OutEvent<DetailViewModel> = outEvent()
    override val error: OutEvent<Error> = outEvent()
    override val startProcess: OutEventU = outEvent()

    override val loadDetails: InEvent<Int> = coInEvent {
        startProcess()
        delay(1500)
        if (it >= 0) modelLoaded(DetailViewModel(it, "I'm $it"))
        else error(Error("Negative ID"))
    }
}
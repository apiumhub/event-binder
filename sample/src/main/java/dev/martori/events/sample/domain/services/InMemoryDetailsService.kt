package dev.martori.events.sample.domain.services

import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.core.coInEvent
import dev.martori.events.core.outEvent
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailViewModel
import kotlinx.coroutines.delay

class InMemoryDetailsService : DetailsService {

    override val sendState: OutEvent<AsyncView<DetailViewModel>> = outEvent()

    override val loadDetails: InEvent<Int> = coInEvent {
        sendState(AsyncView.Loading())
        delay(1500)
        if (it >= 0) sendState(AsyncView.Success(DetailViewModel(it, "I'm $it")))
        else sendState(AsyncView.Error(Error("Negative ID")))
    }
}
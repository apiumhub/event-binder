package dev.martori.events.sample.domain.services

import dev.martori.events.core.Consumer
import dev.martori.events.core.Event
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendConsumer
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailViewModel
import kotlinx.coroutines.delay

class InMemoryDetailsService : DetailsService {

    override val sendState: Event<AsyncView<DetailViewModel>> = event()

    override val loadDetails: Consumer<Int> = suspendConsumer {
        sendState(AsyncView.Loading())
        delay(1500)
        if (it >= 0) sendState(AsyncView.Success(DetailViewModel(it, "I'm $it")))
        else sendState(AsyncView.Error(Error("Negative ID")))
    }
}
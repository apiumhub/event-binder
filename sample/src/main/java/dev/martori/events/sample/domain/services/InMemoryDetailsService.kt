package dev.martori.events.sample.domain.services

import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.domain.entities.Details
import kotlinx.coroutines.delay

class InMemoryDetailsService : DetailsService {

    override val sendState: Event<AsyncView<Details>> = event()

    override val loadDetails: Receiver<Int> = suspendReceiver {
        sendState(AsyncView.Loading())
        delay(1500)
        if (it >= 0) sendState(AsyncView.Success(Details(it, "I'm $it")))
        else sendState(AsyncView.Error(Error("Negative ID")))
    }
}
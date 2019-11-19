package dev.martori.events.sample.domain.services

import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailViewModel
import dev.martori.events.sample.data.network.api.DetailsApi
import dev.martori.events.sample.data.network.api.DetailsDto

class RemoteDetailsService(api: DetailsApi) : DetailsService {
    override val loadDetails: Receiver<Int> = suspendReceiver { id ->
        sendState(AsyncView.Loading())
        runCatching { api.getDetails(id) }.fold({ details ->
            sendState(AsyncView.Success(details.toViewModel()))
        }, {
            sendState(AsyncView.Error(Error(it)))
        })
    }
    override val sendState: Event<AsyncView<DetailViewModel>> = event()
}

private fun DetailsDto.toViewModel() = DetailViewModel(id, name)
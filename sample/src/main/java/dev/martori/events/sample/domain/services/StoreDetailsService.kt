package dev.martori.events.sample.domain.services

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailViewModel
import dev.martori.events.sample.data.network.api.DetailsDto

typealias DetailsStore = Store<Int, DetailsDto>

class StoreDetailsService(store: DetailsStore) : DetailsService {
    override val sendState: Event<AsyncView<DetailViewModel>> = event()

    override val loadDetails: Receiver<Int> = suspendReceiver { id ->
        sendState(AsyncView.Loading())
        runCatching { store.fresh(id) }.fold({ details ->
            sendState(AsyncView.Success(details.toViewModel()))
        }, {
            sendState(AsyncView.Error(Error(it)))
        })
    }
}

private fun DetailsDto.toViewModel() = DetailViewModel(id, name)
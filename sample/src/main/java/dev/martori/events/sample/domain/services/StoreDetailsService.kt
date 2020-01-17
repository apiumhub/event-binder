package dev.martori.events.sample.domain.services

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.event
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.domain.entities.Details

typealias DetailsStore = Store<Int, Details>

class StoreDetailsService(store: DetailsStore) : DetailsService {
    override val sendState: Event<AsyncView<Details>> = event()
    override val loadDetails: Receiver<Int> = suspendReceiver { id ->
        sendState(AsyncView.Loading())
        runCatching { store.fresh(id) }.fold({ details ->
            sendState(AsyncView.Success(details))
        }, {
            sendState(AsyncView.Error(Error(it)))
        })
    }
}
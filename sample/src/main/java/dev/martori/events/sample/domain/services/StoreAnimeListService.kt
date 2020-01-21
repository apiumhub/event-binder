package dev.martori.events.sample.domain.services

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.get
import dev.martori.events.core.*
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.models.AnimeRequestByYear
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.domain.entities.Anime

class StoreAnimeListService(store: Store<AnimeRequestByYear, List<Anime>>) : AnimeListService {
    override val loadAnimeByYear: Receiver<AnimeRequestByYear> = suspendReceiver {
        startFetching()
        runCatching { store.get(it) }.fold({
            animeListReceived(it)
        }, {
            errorReceived(Error(it))
        })
    }
    override val errorReceived: Event<Error> = event(retainValue = false)
    override val startFetching: EventU = event(retainValue = false)
    override val animeListReceived: Event<List<Anime>> = event()
}
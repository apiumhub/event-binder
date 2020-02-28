package dev.martori.events.sample.domain.services

import dev.martori.events.core.*
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.binding.services.AnimeListService
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.AnimeRepository


class StoreAnimeListService(repository: AnimeRepository) : AnimeListService {
    override val loadAnime: Receiver<AnimeListRequest> = suspendReceiver {
        startFetching()
        runCatching { repository.getList(it) }.fold({
            animeListReceived(it)
        }, {
            errorReceived(Error(it))
        })
    }
    override val errorReceived: Event<Error> = event(retainValue = false)
    override val startFetching: EventU = event(retainValue = false)
    override val animeListReceived: Event<List<Anime>> = event()
}
package com.apiumhub.events.sample.domain.services

import com.apiumhub.events.core.*
import com.apiumhub.events.coroutines.suspendReceiver
import com.apiumhub.events.sample.binding.models.AnimeListRequest
import com.apiumhub.events.sample.binding.services.AnimeListService
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.repositories.AnimeRepository


class NetworkAnimeListService(repository: AnimeRepository) : AnimeListService {
    override val loadAnime: Receiver<AnimeListRequest> = suspendReceiver {
        startedFetching()
        runCatching { repository.getList(it) }.fold({
            animeListReceived(it)
        }, {
            errorReceived(Error(it))
        })
    }
    override val errorReceived: Event<Error> = event(retainValue = false)
    override val startedFetching: EventU = event(retainValue = false)
    override val animeListReceived: Event<List<Anime>> = event()
}
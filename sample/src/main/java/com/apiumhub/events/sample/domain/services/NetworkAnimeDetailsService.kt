package com.apiumhub.events.sample.domain.services

import com.apiumhub.events.core.*
import com.apiumhub.events.coroutines.suspendReceiver
import com.apiumhub.events.sample.binding.models.AnimeRequest
import com.apiumhub.events.sample.binding.services.AnimeDetailsService
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id
import com.apiumhub.events.sample.domain.repositories.AnimeRepository

class NetworkAnimeDetailsService(repository: AnimeRepository) : AnimeDetailsService {
    override val loadAnime: Receiver<Id> = suspendReceiver { id: Id ->
        startedFetching()
        runCatching {
            repository.get(AnimeRequest(id))
        }.fold(onSuccess = {
            animeReceived(it)
        }, onFailure = {
            errorReceived(Error(it))
        })
    }
    override val errorReceived: Event<Error> = event()
    override val startedFetching: EventU = event()
    override val animeReceived: Event<Anime> = event()
}
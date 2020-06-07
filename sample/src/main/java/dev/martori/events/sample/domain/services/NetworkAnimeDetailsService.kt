package dev.martori.events.sample.domain.services

import dev.martori.events.core.*
import dev.martori.events.coroutines.suspendReceiver
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.binding.services.AnimeDetailsService
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id
import dev.martori.events.sample.domain.repositories.AnimeRepository

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
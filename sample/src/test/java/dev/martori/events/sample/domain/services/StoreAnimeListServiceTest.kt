package dev.martori.events.sample.domain.services

import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.Repository
import dev.martori.events.test.withParameter
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class StoreAnimeListServiceTest {
    private val repository: Repository<AnimeRequest, List<Anime>> = mockk(relaxed = true)
    private val sut = StoreAnimeListService(repository)

    private val request = AnimeRequest()

    @Test
    fun `loadanime makes a request to the repository with the provided request`() = runBlockingTest {
        (sut.loadAnime withParameter request).dispatch()
        coVerify { repository.get(request) }
    }
}
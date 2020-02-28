package dev.martori.events.sample.domain.services

import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.Repository
import dev.martori.events.test.Parameter
import dev.martori.events.test.shouldDispatch
import dev.martori.events.test.withParameter
import io.mockk.coEvery
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

    @Test
    fun `load anime dispatches fetch event`() {
        sut.loadAnime withParameter request shouldDispatch {
            sut.startFetching withParameter Unit
        }
    }

    @Test
    fun `load anime dispatches fetch and error events if request fails`() {
        coEvery { repository.get(any()) } throws Error()

        sut.loadAnime withParameter request shouldDispatch {
            sut.startFetching withParameter Unit
            sut.errorReceived withAny Parameter
        }
    }

    @Test
    fun `load anime dispatches fetch and success events if request returns`() {
        val result = listOf<Anime>()
        coEvery { repository.get(any()) } returns result

        sut.loadAnime withParameter request shouldDispatch {
            sut.startFetching withParameter Unit
            sut.animeListReceived withParameter result
        }
    }
}
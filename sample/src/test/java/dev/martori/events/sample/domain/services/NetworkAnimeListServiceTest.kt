package dev.martori.events.sample.domain.services

import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.repositories.AnimeRepository
import dev.martori.events.test.shouldDispatch
import dev.martori.events.test.withParameter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class NetworkAnimeListServiceTest {
    private val repository: AnimeRepository = mockk(relaxed = true)
    private val sut = NetworkAnimeListService(repository)

    private val request = AnimeListRequest()

    @Test
    fun `loadanime makes a request to the repository with the provided request`() = runBlockingTest {
        (sut.loadAnime withParameter request).dispatch()
        coVerify { repository.getList(request) }
    }

    @Test
    fun `load anime dispatches fetch event`() {
        sut.loadAnime withParameter request shouldDispatch {
            sut.startedFetching withParameter Unit
        }
    }

    @Test
    fun `load anime dispatches fetch and error events if request fails`() {
        val error =  Error()
        coEvery { repository.getList(any()) } throws error

        sut.loadAnime withParameter request shouldDispatch {
            sut.startedFetching withParameter Unit
            sut.errorReceived withType Error::class
            sut.errorReceived assertOverParameter {
                assertEquals(error, it.cause)
            }
        }
    }

    @Test
    fun `load anime dispatches fetch and success events if request returns`() {
        val result = listOf<Anime>()
        coEvery { repository.getList(any()) } returns result

        sut.loadAnime withParameter request shouldDispatch {
            sut.startedFetching withParameter Unit
            sut.animeListReceived withParameter result
        }
    }
}
package dev.martori.events.sample.domain.services

import dev.martori.events.sample.binding.views.DetailViewModel
import dev.martori.events.test.Dispatched
import dev.martori.events.test.Parameter
import dev.martori.events.test.shouldDispatch
import dev.martori.events.test.withParameter
import org.junit.Test

class InMemoryDetailsServiceTest {

    private val sut = InMemoryDetailsService()

    @Test
    fun `loading details should return the id parsed into a name`() {
        val id = 0
        val expected = DetailViewModel(id, "I'm $id")

        sut.loadDetails withParameter id shouldDispatch {
            sut.startProcess withAny Parameter
            sut.modelLoaded withParameter expected
            sut.error never Dispatched
        }
    }

    @Test
    fun `loading details should dispatch error if id is negative`() {
        val id = -1

        sut.loadDetails withParameter id shouldDispatch {
            sut.startProcess withAny Parameter
            sut.error withAny Parameter
        }
    }
}
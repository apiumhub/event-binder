package dev.martori.events.sample.domain.services

import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.domain.entities.Details
import dev.martori.events.test.shouldDispatch
import dev.martori.events.test.withParameter
import org.junit.Test

class InMemoryDetailsServiceTest {

    private val sut = InMemoryDetailsService()

    @Test
    fun `loading details should return the id parsed into a name`() {
        val id = 0
        val expected = AsyncView.Success(Details(id, "I'm $id"))

        sut.loadDetails withParameter id shouldDispatch {
            sut.sendState withType AsyncView.Loading::class
            sut.sendState withParameter expected
        }
    }

    @Test
    fun `loading details should dispatch error if id is negative`() {
        sut.loadDetails withParameter -1 shouldDispatch {
            sut.sendState withType AsyncView.Loading::class
            sut.sendState withType AsyncView.Error::class
        }
    }

}
package dev.martori.events.sample.data.inmemory

import dev.martori.events.sample.binding.views.DetailViewModel
import dev.martori.events.test.shouldDispatch
import dev.martori.events.test.withParameter
import org.junit.Test

class InMemoryDetailsServiceTest {

    val sut = InMemoryDetailsService()

    @Test
    fun `loading details should return the id parsed into a name`() {
        val id = 0
        val expected = DetailViewModel(id, "I'm $id")
        sut.loadDetails withParameter id shouldDispatch {
            sut.modelLoaded withParameter expected
        }
    }
}
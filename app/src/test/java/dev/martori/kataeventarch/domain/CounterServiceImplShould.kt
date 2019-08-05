package dev.martori.kataeventarch.domain

import cat.martori.core.Bindable
import cat.martori.eventarchtest.BindAllTestsRule
import cat.martori.eventarchtest.dispatchedWith
import cat.martori.eventarchtest.implies
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CounterServiceImplShould : Bindable {

    private val repo = mockk<CounterRepository>()
    private val sut = CounterServiceImpl(repo)

    @get:Rule
    val bindRule = BindAllTestsRule

    @Before
    fun setUp() {
        coEvery {
            repo.getNewCount()
        } returns 3
    }

    @Test
    fun `test example`() {
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(3, it) }
        }

        sut.modifyCounter dispatchedWith (Unit) implies {
            sut.totalCount dispatchedWith { assertEquals(3, it) }
        }

//        sut.modifyCounter implies {
//            sut.totalCount never Dispatched
//        }

    }

}

package dev.martori.kataeventarch.domain

import cat.martori.core.*
import cat.martori.eventarchtest.dispatchedWith
import cat.martori.eventarchtest.implies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CounterServiceImplShould : Bindable {

    private val sut = CounterServiceImpl()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test example 2`() = runBlockingTest {
        val localEvent: OutEventU = outEvent()
        bind {
            sut.modifyCounter via localEvent
            sut.totalCount via inEvent<Int> {
                assertEquals(3, it)
                unbind()
            }
        }

        localEvent()
    }

    @Test
    fun `test example 5`() = runBlockingTest {
        val localEvent: OutEventU = outEvent()
        bind {
            localEvent()
            sut.modifyCounter via localEvent
            sut.totalCount via inEvent<Int> {
                assertEquals(2, it)
            }
        }.unbind()

    }

    @Test
    fun `test example 7`() {
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(1, it) }
        }

        sut.modifyCounter dispatchedWith (Unit) implies {
            sut.totalCount dispatchedWith { assertEquals(2, it) }
        }

//        sut.modifyCounter implies {
//            sut.totalCount never Dispatched
//        }

    }
}

package dev.martori.kataeventarch.domain

import cat.martori.core.*
import cat.martori.eventarchtest.Dispatched
import cat.martori.eventarchtest.implies
import cat.martori.eventarchtest.testBind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class CounterServiceImplShould : Bindable {

    private val sut = CounterServiceImpl()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test example`() = runBlockingTest {
        bind {
            sut.totalCount via inEvent<Int> {
                assertEquals(1, it)
                unbind()
            }
        }

        sut.modifyCounter.dispatch()
    }

    @Test
    fun `test example 2`() = runBlockingTest {
        val localEvent: OutEventU = outEvent()
        bind {
            sut.modifyCounter via localEvent
            sut.totalCount via inEvent<Int> {
                assertEquals(1, it)
                unbind()
            }
        }

        localEvent()
    }

    @Test
    fun `test example 3`() = runBlockingTest {
        bind {
            sut.totalCount via inEvent<Int> {
                assertEquals(1, it)
                unbind()
            }
        }

        sut.modifyCounterTest()
    }

    @Test
    fun `test example 5`() = runBlockingTest {
        bind {
            sut.totalCount via inEvent<Int> {
                assert(true)
            }
            sut.totalCount via inEvent<Int> {
                fail()
            }
            sut.modifyCounter.dispatch()
        }.unbind()
    }

    @Test
    fun `test example 6`() = testBind {
        sut.modifyCounter.dispatch()
        sut.totalCount dispatchedWith {
            assertEquals(1, it)
        }
        sut.totalCount never Dispatched
    }

    @Test
    fun `test example 7`() {
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(1, it) }
//            sut.totalCount never Dispatched
        }
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(2, it) }
            sut.totalCount never Dispatched
        }
        sut.modifyCounter implies {
            sut.totalCount dispatchedWith { assertEquals(3, it) }
//            sut.totalCount never Dispatched
        }
    }
}

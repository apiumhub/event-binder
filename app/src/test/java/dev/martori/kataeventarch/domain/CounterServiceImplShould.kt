package dev.martori.kataeventarch.domain

import cat.martori.core.*
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
        sut.totalCount checkIt {
            assertEquals(1, it)
        }
        sut.totalCount checkIt {
            //                fail()
        }
        sut.modifyCounter.dispatch()
    }

}

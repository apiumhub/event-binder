package dev.martori.kataeventarch.domain

import dev.martori.eventarch.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
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
    fun `test example 4`() = runBlockingTest {
        val localEvent: OutEventU = outEvent()
        bind {
            sut.modifyCounter via localEvent
            sut.totalCount via verify<Int> {
                assertEquals(1, it)
            }
        }
        localEvent()
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
        checkIt(sut.totalCount) {
            assertEquals(1, it)
        }
        checkIt(sut.totalCount) {

        }
    }

    val flags = mutableListOf<String>()

    fun <T> Binder.checkIt(
        outEvent: OutEvent<T>,
        block: (T) -> Unit
    ) { //TODO move this to a testBind implementation
        flags.plusAssign(outEvent.toString())
        outEvent via inEvent<T> {
            flags.removeAt(0)
            block(it)
        }
    }

    private fun testBind(block: Binder.() -> Unit) = runBlockingTest {
        bind(block).unbind()
        if (flags.isNotEmpty()) fail("${flags[0]} was not called")
    }

    private infix fun <T> InEvent<T>.should(block: Binder.() -> T) {
        bind {
            block()
        }.unbind()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `BroadcastChannel keep a coroutine alive`() = runBlockingTest {
        subscribe {
            //work with the values
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `BroadcastChannel keep a coroutine alive 2`() = runBlockingTest {
        val out = outEvent<Int>()
        val ine = inEvent<Int> { println(it) }
        bind {
            out via ine
            out via ine
            out via ine
            out via ine
            out(4)
        }.unbind()
    }

    var job: Job? = null

    private val channel = BroadcastChannel<Int>(Channel.CONFLATED)


    fun CoroutineScope.send(value: Int) {
        launch {
            channel.send(value)
        }
    }

    fun CoroutineScope.subscribe(block: (Int) -> Unit) {
        val flow = channel.asFlow()
        job = launch {
            flow.collect { block(it) }
        }
    }

    private fun <T> Binder.verify(block: (T) -> Unit): InEvent<T> =
        (object : Bindable {}).inEvent {
            block(it)
            unbind()
        }


}

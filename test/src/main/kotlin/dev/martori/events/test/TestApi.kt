package dev.martori.events.test

import dev.martori.events.core.Binder
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.coroutines.CoBindable
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.inEvent
import kotlinx.coroutines.test.runBlockingTest


private var counter = 0

interface TestBinder : Binder, CoBindable {

    infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        this via inEvent {
            counter--
            block(it)
        }
    }

    infix fun <T> OutEvent<T>.withParameter(param: T) {
        counter++
        this via inEvent {
            counter--
            assert(it == param)
        }
    }

    infix fun <T> OutEvent<T>.withAny(param: Parameter) {
        counter++
        this via inEvent {
            counter--
        }
    }

    infix fun <T> OutEvent<T>.never(dispatched: Dispatched) {
        this via inEvent { throw Error("Dispatched an OutEvent that should not be dispatched") }
    }
}


fun testBind(block: suspend TestBinder.() -> Unit) = runBlockingTest {
    val binded = bind { }
    val testB = object : TestBinder, CoBindable by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (counter != 0) throw Error("There were $counter wanted but not dispatched OutEvent")
}

object Implies
object Dispatched
object Parameter

infix fun <T> InEvent<T>.withParameter(data: T) = runBlockingTest { dispatch(data) }.let { Implies }

infix fun Implies.shouldDispatch(block: suspend TestBinder.() -> Unit) = testBind { block() }

infix fun InEventU.shouldDispatch(block: suspend TestBinder.() -> Unit) = withParameter(Unit) shouldDispatch block




package dev.martori.events.test

import dev.martori.events.core.*
import dev.martori.events.coroutines.CoBindable
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.inEvent
import kotlinx.coroutines.test.runBlockingTest


private var counter = 0

interface TestBinder : Binder, CoBindable {

    infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        this via inEvent<T> {
            counter--
            block(it)
        }
    }

    infix fun <T> OutEvent<T>.withParameter(param: T) {
        counter++
        this via inEvent<T> {
            counter--
            assert(it == param)
        }
    }

    infix fun <T> OutEvent<T>.withAny(param: Parameter) {
        counter++
        this via inEvent<T> {
            counter--
        }
    }

    infix fun <T> OutEvent<T>.never(dispatched: Dispatched) {
        this via inEvent<T> { throw Error("Dispatched an OutEvent that should not be dispatched") }
    }
}


fun testBind(block: TestBinder.() -> Unit) = runBlockingTest {
    val binded = bind { }
    val testB = object : TestBinder, CoBindable by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (counter != 0) throw Error("There were $counter wanted but not dispatched OutEvent")
}

object Implies
object Dispatched
object Parameter

infix fun <T> InEvent<T>.withParameter(data: T) = dispatch(data).let { Implies }

infix fun InEventU.shouldDispatch(block: TestBinder.() -> Unit) = dispatch().also { testBind(block) }


infix fun Implies.shouldDispatch(block: TestBinder.() -> Unit) = testBind(block)




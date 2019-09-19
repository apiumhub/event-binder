package dev.martori.events.test

import dev.martori.events.core.Binder
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.coroutines.CoBindable
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.inEvent
import kotlinx.coroutines.test.runBlockingTest


interface TestBinder : Binder, CoBindable {
    infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit)
    infix fun <T> OutEvent<T>.withParameter(param: T)
    infix fun <T> OutEvent<T>.withAny(param: Parameter)
    infix fun <T> OutEvent<T>.never(dispatched: Dispatched)
}

typealias Assertion<T> = (T) -> Unit

internal class TestBinderInternal(scope: CoBindable, binder: Binder) : TestBinder, Binder by binder, CoBindable by scope {

    private val assertions = mutableMapOf<OutEvent<*>, List<Assertion<*>>>()
    var counter = 0

    override infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        val list = assertions.getOrPut(this) { mutableListOf<Assertion<T>>() } as? MutableList<Assertion<T>>
        list?.add(block)
        this via inEvent {
            list?.get(0)?.invoke(it)
            list?.removeAt(0)
            counter--
        }
    }

    override infix fun <T> OutEvent<T>.withParameter(param: T) {
        counter++
        this via inEvent {
            counter--
            assert(it == param)
        }
    }

    override infix fun <T> OutEvent<T>.withAny(param: Parameter) {
        counter++
        this via inEvent {
            counter--
        }
    }

    override infix fun <T> OutEvent<T>.never(dispatched: Dispatched) {
        this via inEvent { throw Error("Dispatched an OutEvent that should not be dispatched") }
    }
}


fun testBind(block: suspend TestBinder.() -> Unit) = testBind({}, block)

private fun testBind(dispatch: suspend () -> Unit, block: suspend TestBinder.() -> Unit) = runBlockingTest {
    val testB = TestBinderInternal(this, bind { })
    testB.block()
    dispatch()
    testB.unbind()
    if (testB.counter != 0) throw Error("There were ${testB.counter} wanted but not dispatched OutEvent")
}

class Implies(val dispatch: suspend () -> Unit)
object Dispatched
object Parameter

infix fun <T> InEvent<T>.withParameter(data: T) = Implies { dispatch(data) }

infix fun Implies.shouldDispatch(block: suspend TestBinder.() -> Unit) = testBind(dispatch, block)

infix fun InEventU.shouldDispatch(block: suspend TestBinder.() -> Unit) = withParameter(Unit) shouldDispatch block




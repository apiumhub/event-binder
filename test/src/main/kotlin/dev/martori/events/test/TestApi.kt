package dev.martori.events.test

import dev.martori.events.core.Binder
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.coroutines.CoBindable
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.inEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail
import kotlin.reflect.KClass


interface TestBinder : Binder, CoBindable {
    infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit)
    infix fun <T : Any> OutEvent<T?>.withTypeNullable(type: KClass<*>)
    infix fun <T : Any> OutEvent<T>.withType(type: KClass<*>)
    infix fun <T> OutEvent<T>.withParameter(param: T)
    infix fun <T> OutEvent<T>.withAny(param: Parameter)
    infix fun <T> OutEvent<T>.never(dispatched: Dispatched)
}

typealias Assertion<T> = (T) -> Unit

internal class TestBinderInternal(scope: CoBindable, binder: Binder) : TestBinder, Binder by binder, CoBindable by scope {

    val assertions = mutableMapOf<OutEvent<*>, List<Assertion<*>>>()
    var counter = 0

    override infix fun <T> OutEvent<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        getEventAssertions()?.add {
            counter--
            block(it)
        }
    }

    fun runAssertions() {
        assertions.forEach { (out, list) ->
            var index = 0
            out via inEvent {
                list.getOrElse(index) { {} }(it)
                index++
            }
        }
    }

    fun <T> OutEvent<T>.getEventAssertions() = assertions.getOrPut(this) { mutableListOf<Assertion<T>>() } as? MutableList<Assertion<T>>

    override infix fun <T> OutEvent<T>.withParameter(param: T) {
        counter++
        getEventAssertions()?.add {
            counter--
            assert(it == param) { "value mismatch expected: $param but was $it" }
        }
    }

    override fun <T : Any> OutEvent<T?>.withTypeNullable(type: KClass<*>) {
        counter++
        getEventAssertions()?.add { value ->
            counter--
            value?.let { assert(type.isInstance(it)) { "type mismatch expected: $type but was ${it::class}" } }
        }
    }

    override fun <T : Any> OutEvent<T>.withType(type: KClass<*>) {
        counter++
        getEventAssertions()?.add { value ->
            counter--
            assert(type.isInstance(value)) { "type mismatch expected: $type but was ${value::class}" }
        }
    }

    override infix fun <T> OutEvent<T>.withAny(param: Parameter) {
        counter++
        getEventAssertions()?.add {
            counter--
        }
    }

    override infix fun <T> OutEvent<T>.never(dispatched: Dispatched) {
        this via inEvent { fail("Dispatched an OutEvent that should not be dispatched") }
    }
}


fun testBind(block: suspend TestBinder.() -> Unit) = testBind({}, block)

private fun testBind(dispatch: suspend () -> Unit, block: suspend TestBinder.() -> Unit) = runBlockingTest {
    launch {
        val testB = TestBinderInternal(this, bind { })
        testB.block()
        testB.runAssertions()
        dispatch()
        testB.unbind()
        assert(testB.counter == 0) { "There were ${testB.counter} wanted but not dispatched OutEvent" }
    }
}

class Implies(val dispatch: suspend () -> Unit)
object Dispatched
object Parameter

infix fun <T> InEvent<T>.withParameter(data: T) = Implies { dispatch(data) }

infix fun Implies.shouldDispatch(block: suspend TestBinder.() -> Unit) = testBind(dispatch, block)

infix fun InEventU.shouldDispatch(block: suspend TestBinder.() -> Unit) = withParameter(Unit) shouldDispatch block




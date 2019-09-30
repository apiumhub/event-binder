package dev.martori.events.test

import dev.martori.events.core.Binder
import dev.martori.events.core.Consumer
import dev.martori.events.core.ConsumerU
import dev.martori.events.core.Event
import dev.martori.events.coroutines.CoBindable
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.consumer
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredFunctions

interface TestBinder : Binder, CoBindable {
    infix fun <T> Event<T>.assertOverParameter(block: (T) -> Unit)
    infix fun <T : Any> Event<T?>.withTypeNullable(type: KClass<*>)
    infix fun <T : Any> Event<T>.withType(type: KClass<*>)
    infix fun <T> Event<T>.withParameter(param: T)
    infix fun <T> Event<T>.withAny(param: Parameter)
    infix fun <T> Event<T>.never(dispatched: Dispatched)
}

typealias Assertion<T> = (T) -> Unit

internal class TestBinderInternal(scope: CoBindable, binder: Binder) : TestBinder, Binder by binder, CoBindable by scope {

    private val assertions = mutableMapOf<Event<*>, List<Assertion<*>>>()
    internal var counter = 0

    override infix fun <T> Event<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        getEventAssertions()?.add {
            counter--
            block(it)
        }
    }

    fun runAssertions() {
        assertions.forEach { (out, list) ->
            var index = 0
            out via consumer {
                list.getOrElse(index) { {} }(it)
                index++
            }
        }
    }

    fun <T> Event<T>.getEventAssertions() = assertions.getOrPut(this) { mutableListOf<Assertion<T>>() } as? MutableList<Assertion<T>>

    override infix fun <T> Event<T>.withParameter(param: T) {
        counter++
        getEventAssertions()?.add {
            counter--
            assert(it == param) { "value mismatch expected: $param but was $it" }
        }
    }

    override fun <T : Any> Event<T?>.withTypeNullable(type: KClass<*>) {
        counter++
        getEventAssertions()?.add { value ->
            counter--
            value?.let { assert(type.isInstance(it)) { "type mismatch expected: $type but was ${it::class}" } }
        }
    }

    override fun <T : Any> Event<T>.withType(type: KClass<*>) {
        counter++
        getEventAssertions()?.add { value ->
            counter--
            assert(type.isInstance(value)) { "type mismatch expected: $type but was ${value::class}" }
        }
    }

    override infix fun <T> Event<T>.withAny(param: Parameter) {
        counter++
        getEventAssertions()?.add {
            counter--
        }
    }

    override infix fun <T> Event<T>.never(dispatched: Dispatched) {
        this via consumer { fail("Dispatched an Event that should not be dispatched") }
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
        assert(testB.counter == 0) { "There were ${testB.counter} wanted but not dispatched Event" }
    }
}

class Implies(val dispatch: suspend () -> Unit)
object Dispatched
object Parameter

infix fun <T> Consumer<T>.withParameter(data: T) = Implies {
    this::class.declaredFunctions.find { it.name == "dispatch" }?.callSuspend(this, data)
}

infix fun Implies.shouldDispatch(block: suspend TestBinder.() -> Unit) = testBind(dispatch, block)

infix fun ConsumerU.shouldDispatch(block: suspend TestBinder.() -> Unit) = withParameter(Unit) shouldDispatch block
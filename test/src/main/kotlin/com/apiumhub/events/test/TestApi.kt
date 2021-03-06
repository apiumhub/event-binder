package com.apiumhub.events.test

import com.apiumhub.events.core.Binder
import com.apiumhub.events.core.Event
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.core.ReceiverU
import com.apiumhub.events.coroutines.CoBindable
import com.apiumhub.events.coroutines.bind
import com.apiumhub.events.coroutines.receiver
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
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

internal class TestBinderInternal(scope: CoBindable, binder: Binder) : TestBinder, Binder by binder, CoBindable by scope {

    private val assertions = mutableMapOf<Event<*>, MutableList<(Any?) -> Unit>>()
    internal var counter = 0

    override infix fun <T> Event<T>.assertOverParameter(block: (T) -> Unit) {
        counter++
        getAssertions()?.add {
            counter--
            block(it)
        }
    }

    fun runAssertions() {
        assertions.forEach { (out, list) ->
            out via receiver {
                list.forEach { assertion ->
                    assertion(it)
                }
            }
        }
    }

    private fun <T> Event<T>.getAssertions() = assertions.getOrPut(this) { mutableListOf() } as? MutableList<(T) -> Unit>

    override infix fun <T> Event<T>.withParameter(param: T) {
        counter++
        getAssertions()?.add {
            counter--
            assertEquals("value mismatch expected: $param but was $it", param, it)
        }
    }

    override fun <T : Any> Event<T?>.withTypeNullable(type: KClass<*>) {
        counter++
        getAssertions()?.add { value ->
            counter--
            value?.let { assertTrue("type mismatch expected: $type but was ${it::class}", type.isInstance(it)) }
        }
    }

    override fun <T : Any> Event<T>.withType(type: KClass<*>) {
        counter++
        getAssertions()?.add { value ->
            counter--
            assertTrue("type mismatch expected: $type but was ${value::class}", type.isInstance(value))
        }
    }

    override infix fun <T> Event<T>.withAny(param: Parameter) {
        counter++
        getAssertions()?.add {
            counter--
        }
    }

    override infix fun <T> Event<T>.never(dispatched: Dispatched) {
        this via receiver { fail("Dispatched an Event that should not be dispatched") }
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
        assertEquals("There were ${testB.counter} wanted but not dispatched Event", 0, testB.counter)
    }
}

class Implies(val dispatch: suspend () -> Unit)
object Dispatched
object Parameter

infix fun <T> Receiver<T>.withParameter(data: T) = Implies {
    this::class.declaredFunctions.find { it.name == "dispatch" }?.callSuspend(this, data)
}

infix fun Implies.shouldDispatch(block: suspend TestBinder.() -> Unit) = testBind(dispatch, block)

infix fun ReceiverU.shouldDispatch(block: suspend TestBinder.() -> Unit) = withParameter(Unit) shouldDispatch block
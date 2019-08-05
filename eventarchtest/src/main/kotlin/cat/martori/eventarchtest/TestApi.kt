package cat.martori.eventarchtest

import cat.martori.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


private var counter = 0

interface TestBinder : Binder, ScopeBinder {

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


fun CoroutineScope.testBind(block: TestBinder.() -> Unit) {
    val binded = bind { }
    val testB = object : TestBinder, ScopeBinder by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (counter != 0) throw Error("There were $counter wanted but not dispatched OutEvent")
}

object Implies
object Dispatched
object Parameter

infix fun <T> InEvent<T>.withParameter(data: T): Implies = runBlocking {
    dispatch(data)
    Implies
}

infix fun InEventU.shouldDispatch(block: TestBinder.() -> Unit) = runBlocking {
    dispatch().also { testBind(block) }
}

infix fun Implies.shouldDispatch(block: TestBinder.() -> Unit) = runBlocking { testBind(block) }


object BindAllTestsRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(TestCoroutineDispatcher())
                base.evaluate()
                Dispatchers.resetMain()
            }
        }
    }
}

object BindMarkedTestsRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        val enabled = description
            ?.annotations
            ?.filterIsInstance<Bind>()
            ?.isNotEmpty()
            ?: false

        return if (enabled) BindAllTestsRule.apply(base, description) else base
    }
}

annotation class Bind




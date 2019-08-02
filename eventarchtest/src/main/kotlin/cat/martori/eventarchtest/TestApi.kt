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

    infix fun <T> OutEvent<T>.dispatchedWith(block: (T) -> Unit) {
        counter++
        this via inEvent<T> {
            counter--
            block(it)
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
    if (counter != 0) throw Error("Wanted but not dispatched OutEvent")
}

object Implies
object Dispatched

infix fun <T> InEvent<T>.dispatchedWith(data: T): Implies = runBlocking {
    dispatch(data)
    Implies
}

infix fun InEventU.implies(block: TestBinder.() -> Unit) = runBlocking {
    dispatch().also { testBind(block) }
}

infix fun Implies.implies(block: TestBinder.() -> Unit) = runBlocking { testBind(block) }


object BindTestRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(TestCoroutineDispatcher())
                base?.evaluate()
                Dispatchers.resetMain()
            }
        }
    }
}


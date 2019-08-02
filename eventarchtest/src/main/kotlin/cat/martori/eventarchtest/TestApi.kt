package cat.martori.eventarchtest

import cat.martori.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


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

object Implyer
object Dispatched

infix fun <T> InEvent<T>.dispatchedWith(data: T): Implyer = runBlocking {
    dispatch(data)
    Implyer
}

infix fun InEventU.implies(block: TestBinder.() -> Unit) = runBlocking {
    dispatch().also { testBind(block) }
}

infix fun Implyer.implies(block: TestBinder.() -> Unit) = runBlocking { testBind(block) }



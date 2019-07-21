package cat.martori.eventarchtest

import cat.martori.core.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail


private val flags = mutableListOf<String>()

interface TestBinder : Binder, ScopeBinder {

    infix fun <T> OutEvent<T>.dispatchedWith(block: (T) -> Unit) {
        flags += (toString())
        this via inEvent<T> {
            flags.removeAt(0)
            block(it)
        }
    }

    infix fun <T> OutEvent<T>.never(dispatched: Dispatched) {
        this via inEvent<T> { fail("${toString()} should not be called") }
    }
}


fun testBind(block: TestBinder.() -> Unit) = runBlockingTest {
    val binded = bind { }
    val testB = object : TestBinder, ScopeBinder by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (flags.isNotEmpty()) fail("${flags[0]} was not called")
}

object Implyer
object Dispatched

fun dispatching(block: () -> Unit): Implyer {
    block()
    return Implyer
}

infix fun <T> InEvent<T>.dispatchedWith(data: T): Implyer {
    this.dispatch(data)
    return Implyer
}

infix fun InEventU.implies(block: TestBinder.() -> Unit) = dispatch().also { testBind(block) }

infix fun Implyer.implies(block: TestBinder.() -> Unit) = testBind(block)



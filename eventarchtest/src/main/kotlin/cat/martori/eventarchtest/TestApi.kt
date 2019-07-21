package cat.martori.eventarchtest

import cat.martori.core.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail


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
        this via inEvent<T> { fail("${toString()} should not be called") }
    }
}


fun testBind(block: TestBinder.() -> Unit) = runBlockingTest {
    val binded = bind { }
    val testB = object : TestBinder, ScopeBinder by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (counter != 0) fail("Wanted but not dispatched OutEvent")
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



package cat.martori.eventarchtest

import cat.martori.core.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail


private val flags = mutableListOf<String>()

interface TestBinder : Binder, ScopeBinder {

    infix fun <T> OutEvent<T>.checkIt(block: (T) -> Unit) {
        flags += (toString())
        this via inEvent<T> {
            flags.removeAt(0)
            block(it)
        }
    }
}


fun testBind(block: TestBinder.() -> Unit) = runBlockingTest {
    val binded = bind { }
    val testB = object : TestBinder, ScopeBinder by this, Binder by binded {}
    testB.block()
    binded.unbind()
    if (flags.isNotEmpty()) fail("${flags[0]} was not called")
}
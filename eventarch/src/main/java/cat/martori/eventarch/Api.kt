package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface Bindable

interface InEvent<T>
typealias InEventU = InEvent<Unit>

interface OutEvent<T> {
    operator fun invoke(data: T)
}
typealias OutEventU = OutEvent<Unit>

operator fun OutEventU.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<T>)
    infix fun <T> OutEvent<T>.viaU(inEvent: InEvent<Unit>)
    infix fun <T> InEvent<T>.via(outEvent: OutEvent<T>)
    val binded: Boolean
}

fun <T> Bindable.outEvent(): OutEvent<T> = OutEventInternal(CoroutineScope(Dispatchers.Default))
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

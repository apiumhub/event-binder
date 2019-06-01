package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface Bindable

interface InEvent<T>
typealias UInEvent = InEvent<Unit>

interface OutEvent<T> {
    operator fun invoke(data: T)
}
typealias UOutEvent = OutEvent<Unit>

operator fun UOutEvent.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<T>)
    val binded: Boolean
}

fun <T> Bindable.outEvent(): OutEvent<T> = OutEventInternal(CoroutineScope(Dispatchers.Default))
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

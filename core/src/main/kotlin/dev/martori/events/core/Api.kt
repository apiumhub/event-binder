package dev.martori.events.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

interface Bindable
interface InEvent<T> {
    suspend fun dispatch(value: T)
}
typealias InEventU = InEvent<Unit>

suspend fun InEventU.dispatch() = dispatch(Unit)

interface OutEvent<T> {
    operator fun invoke(data: T)
}
typealias OutEventU = OutEvent<Unit>

operator fun OutEventU.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<T>)
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("viaU")
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<Unit>)

    infix fun <T> InEvent<T>.via(outEvent: OutEvent<T>)
    fun unbind()
}

fun <T> Bindable.outEvent(): OutEvent<T> =
    OutEventInternal(CoroutineScope(Dispatchers.Default))

fun <T> Bindable.inEvent(block: suspend (T) -> Unit): InEvent<T> =
    InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

package dev.martori.events.core

import kotlinx.coroutines.CoroutineScope

interface Bindable

object GlobalBind : Bindable

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

    infix fun <T> InEvent<T>.via(outEvent: OutEvent<T>) = outEvent via this
    fun unbind()
}

fun <T> Bindable.outEvent(): OutEvent<T> = OutEventInternal()
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)
fun <T> Bindable.coInEvent(block: suspend CoroutineScope.(T) -> Unit): InEvent<T> = CoInEventInternal(block)

fun Bindable.bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

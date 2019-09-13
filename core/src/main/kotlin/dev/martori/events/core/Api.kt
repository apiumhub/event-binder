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

    infix fun <T> OutEvent<T>.viaU(inEvent: InEvent<Unit>)

    infix fun <T> InEvent<T>.via(outEvent: OutEvent<T>) = outEvent via this
    fun unbind()
    var resumed: Boolean
}

fun <T> Bindable.outEvent(retainValue: Boolean = true): OutEvent<T> = OutEventInternal(retainValue)
fun <T> Bindable.singleTimeOutEvent(): OutEvent<T> = SingleTimeOutEventInternal()
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)
fun <T> Bindable.coInEvent(block: suspend CoroutineScope.(T) -> Unit): InEvent<T> = CoInEventInternal(block)

fun Bindable.bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

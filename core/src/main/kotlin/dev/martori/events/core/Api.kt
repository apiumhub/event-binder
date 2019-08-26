package dev.martori.events.core

interface Bindable

object GlobalBind : Bindable

interface InEvent<T> {
    fun dispatch(value: T)
}
typealias InEventU = InEvent<Unit>

fun InEventU.dispatch() = dispatch(Unit)

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
    OutEventInternal()

fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

fun Bindable.bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

package dev.martori.kataeventarch.ui

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


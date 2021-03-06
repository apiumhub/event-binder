package com.apiumhub.events.core

interface Bindable

object GlobalBind : Bindable

interface Receiver<T>
typealias ReceiverU = Receiver<Unit>

@RequiresOptIn("This API is work in progress, it may or may not work and is subject to change at any time (even being removed entirely)")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class WorkInProgress

interface Event<T> {
    operator fun invoke(data: T)
    fun <R> map(block: (T) -> R): Event<R>
    fun filter(block: (T) -> Boolean): Event<T>
}
typealias EventU = Event<Unit>

operator fun EventU.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> Event<T>.via(receiver: Receiver<T>)

    infix fun <T> Event<T>.viaU(receiver: Receiver<Unit>)

    infix fun <T> Receiver<T>.via(event: Event<T>) = event via this
    fun unbind()
    var resumed: Boolean
}

fun <T> Bindable.event(retainValue: Boolean = true): Event<T> = EventInternal(retainValue)
@WorkInProgress
fun <T> Bindable.singleTimeEvent(): Event<T> = SingleTimeEventInternal()
fun <T> Bindable.receiver(block: (T) -> Unit): Receiver<T> = ReceiverInternal(block)

fun Bindable.bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }
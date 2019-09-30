package dev.martori.events.core

interface Bindable

object GlobalBind : Bindable

interface Consumer<T>
typealias ConsumerU = Consumer<Unit>

interface Event<T> {
    operator fun invoke(data: T)
}
typealias EventU = Event<Unit>

operator fun EventU.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> Event<T>.via(consumer: Consumer<T>)

    infix fun <T> Event<T>.viaU(consumer: Consumer<Unit>)

    infix fun <T> Consumer<T>.via(event: Event<T>) = event via this
    fun unbind()
    var resumed: Boolean
}

fun <T> Bindable.event(retainValue: Boolean = true): Event<T> = EventInternal(retainValue)
fun <T> Bindable.singleTimeEvent(): Event<T> = SingleTimeEventInternal()
fun <T> Bindable.consumer(block: (T) -> Unit): Consumer<T> = ConsumerInternal(block)

fun Bindable.bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }
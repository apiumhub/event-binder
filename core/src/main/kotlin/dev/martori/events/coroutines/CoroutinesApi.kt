package dev.martori.events.coroutines

import dev.martori.events.core.*
import kotlinx.coroutines.CoroutineScope

typealias CoBindable = CoroutineScope

@JvmName("extBind")
fun CoBindable.bind(bindBlock: Binder.() -> Unit): Binder = bind(this, bindBlock)

fun bind(coroutineScope: CoBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    coroutineScope?.binder?.apply(bindBlock) ?: GlobalBind.bind(bindBlock)

fun <T> CoBindable.event(retainValue: Boolean = true): Event<T> = EventInternal(retainValue)
fun <T> CoBindable.singleTimeEvent(): Event<T> = SingleTimeEventInternal()
fun <T> CoBindable.consumer(block: (T) -> Unit): Consumer<T> = ConsumerInternal(block)
fun <T> CoBindable.suspendConsumer(block: suspend CoroutineScope.(T) -> Unit): Consumer<T> = SuspendConsumerInternal(block, this)

fun <T> Bindable.suspendConsumer(block: suspend CoroutineScope.(T) -> Unit): Consumer<T> = SuspendConsumerInternal(block)
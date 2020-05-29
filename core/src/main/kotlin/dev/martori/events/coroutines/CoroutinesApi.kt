package dev.martori.events.coroutines

import dev.martori.events.core.*
import kotlinx.coroutines.CoroutineScope

typealias CoBindable = CoroutineScope

@JvmName("extBind")
fun CoBindable.bind(bindBlock: Binder.() -> Unit): Binder = bind(this, bindBlock)

fun bind(coroutineScope: CoBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    coroutineScope?.binder?.apply(bindBlock) ?: GlobalBind.bind(bindBlock)

fun <T> CoBindable.event(retainValue: Boolean = true): Event<T> = EventInternal(retainValue)
@WorkInProgress
fun <T> CoBindable.singleTimeEvent(): Event<T> = SingleTimeEventInternal()
fun <T> CoBindable.receiver(block: (T) -> Unit): Receiver<T> = ReceiverInternal(block)
fun <T> CoBindable.suspendReceiver(block: suspend CoroutineScope.(T) -> Unit): Receiver<T> = SuspendReceiverInternal(block, this)

fun <T> Bindable.suspendReceiver(block: suspend CoroutineScope.(T) -> Unit): Receiver<T> = SuspendReceiverInternal(block)
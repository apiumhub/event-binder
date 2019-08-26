package dev.martori.events.coroutines

import dev.martori.events.core.*
import kotlinx.coroutines.CoroutineScope

typealias CoBindable = CoroutineScope

@JvmName("extBind")
fun CoBindable.bind(bindBlock: Binder.() -> Unit): Binder =
    bind(this, bindBlock)

fun bind(coroutineScope: CoBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    coroutineScope?.binder?.apply(bindBlock) ?: GlobalBind.bind(bindBlock)

fun <T> CoBindable.outEvent(): OutEvent<T> = OutEventInternal()

fun <T> CoBindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)
fun <T> CoBindable.coInEvent(block: suspend CoroutineScope.(T) -> Unit): InEvent<T> = CoInEventInternal(this, block)
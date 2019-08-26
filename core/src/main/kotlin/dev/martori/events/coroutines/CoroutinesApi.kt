package dev.martori.events.coroutines

import dev.martori.events.core.*
import kotlinx.coroutines.CoroutineScope

typealias ScopeBinder = CoroutineScope

@JvmName("extBind")
fun ScopeBinder.bind(bindBlock: Binder.() -> Unit): Binder =
    bind(this, bindBlock)

fun bind(coroutineScope: ScopeBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    coroutineScope?.binder?.apply(bindBlock) ?: bind(bindBlock)

fun <T> ScopeBinder.outEvent(): OutEvent<T> =
    OutEventInternal()

fun <T> ScopeBinder.inEvent(block: suspend (T) -> Unit): InEvent<T> =
    InEventInternal(block)
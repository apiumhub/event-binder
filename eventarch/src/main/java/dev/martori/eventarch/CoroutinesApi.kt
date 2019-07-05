package dev.martori.eventarch

import kotlinx.coroutines.CoroutineScope

typealias ScopeBinder = CoroutineScope

@JvmName("extBind")
fun ScopeBinder.bind(bindBlock: Binder.() -> Unit): Binder = bind(this, bindBlock)

fun bind(coroutineScope: ScopeBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    coroutineScope?.binder?.apply(bindBlock) ?: bind(bindBlock)

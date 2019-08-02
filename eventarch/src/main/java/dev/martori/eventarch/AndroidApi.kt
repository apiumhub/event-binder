package dev.martori.eventarch

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cat.martori.core.*

typealias ViewBinder = LifecycleOwner


fun <T> ViewBinder.outEvent(): OutEvent<T> = lifecycleScope.outEvent()
fun <T> ViewBinder.inEvent(block: suspend (T) -> Unit): InEvent<T> = lifecycleScope.inEvent(block)

@JvmName("extBind")
fun ViewBinder.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.lifecycleScope?.bind {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: bind(bindBlock)
package dev.martori.events.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import dev.martori.events.core.Binder
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.inEvent
import dev.martori.events.coroutines.outEvent

typealias ViewBinder = LifecycleOwner


fun <T> ViewBinder.outEvent(): OutEvent<T> = lifecycleScope.outEvent()
fun <T> ViewBinder.inEvent(block: suspend (T) -> Unit): InEvent<T> = lifecycleScope.inEvent(block)

@JvmName("extBind")
fun ViewBinder.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.lifecycleScope?.bind {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: dev.martori.events.core.bind(bindBlock)
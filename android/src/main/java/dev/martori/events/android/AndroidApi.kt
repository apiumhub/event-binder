package dev.martori.events.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import dev.martori.events.core.*
import dev.martori.events.coroutines.bind
import dev.martori.events.coroutines.coInEvent
import dev.martori.events.coroutines.inEvent
import dev.martori.events.coroutines.outEvent
import kotlinx.coroutines.CoroutineScope

typealias ViewBindable = LifecycleOwner

fun <T> ViewBindable.outEvent(): OutEvent<T> = lifecycleScope.outEvent()
fun <T> ViewBindable.inEvent(block: (T) -> Unit): InEvent<T> = lifecycleScope.inEvent(block)
fun <T> ViewBindable.coInEvent(block: suspend CoroutineScope.(T) -> Unit): InEvent<T> = lifecycleScope.coInEvent(block)

@JvmName("extBind")
fun ViewBindable.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.lifecycleScope?.bind {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: GlobalBind.bind(bindBlock)
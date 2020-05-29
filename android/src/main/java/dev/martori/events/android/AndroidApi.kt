package dev.martori.events.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import dev.martori.events.core.*
import dev.martori.events.coroutines.*
import kotlinx.coroutines.CoroutineScope

typealias ViewBindable = LifecycleOwner

fun <T> ViewBindable.event(retainValue: Boolean = true): Event<T> = lifecycleScope.event(retainValue)
@WorkInProgress
fun <T> ViewBindable.singleTimeEvent(): Event<T> = lifecycleScope.singleTimeEvent()
fun <T> ViewBindable.receiver(block: (T) -> Unit): Receiver<T> = lifecycleScope.receiver(block)
fun <T> ViewBindable.suspendReceiver(block: suspend CoroutineScope.(T) -> Unit): Receiver<T> = lifecycleScope.suspendReceiver(block)

@JvmName("extBind")
fun ViewBindable.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.lifecycleScope?.bind {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: GlobalBind.bind(bindBlock)
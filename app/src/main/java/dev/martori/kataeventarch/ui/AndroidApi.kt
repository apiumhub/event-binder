package dev.martori.kataeventarch.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent

typealias ViewBinder = LifecycleOwner

@JvmName("uOutEvent")
fun LifecycleOwner.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> LifecycleOwner.outEvent(): OutEvent<T> = binder.newOutEvent()
fun LifecycleOwner.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> LifecycleOwner.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

@JvmName("extBind")
fun LifecycleOwner.bind(bindBlock: Binder.() -> Unit) = bind(this, bindBlock)

fun bind(lifecycleOwner: LifecycleOwner? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.binder?.apply {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: bind(bindBlock)

private fun InternalBinder.lifecycleObserver(bindBlock: Binder.() -> Unit): LifecycleObserver =
    object : LifecycleObserver {
        init {
            bindBlock()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun bind() {
            binded = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun unBind() {
            binded = false
        }
    }

internal val lifecycleBinders = mutableMapOf<LifecycleOwner, InternalBinder>()
internal val LifecycleOwner.binder: InternalBinder
    get() = lifecycleBinders[this] ?: InternalBinder().also { lifecycleBinders[this] = it }
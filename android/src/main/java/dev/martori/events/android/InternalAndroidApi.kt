package dev.martori.events.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dev.martori.events.core.Binder

internal fun Binder.lifecycleObserver(bindBlock: Binder.() -> Unit): LifecycleObserver =
    object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            bindBlock()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            unbind()
        }
    }

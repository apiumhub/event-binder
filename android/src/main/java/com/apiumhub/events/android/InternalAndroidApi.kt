package com.apiumhub.events.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.apiumhub.events.core.Binder

internal fun Binder.lifecycleObserver(bindBlock: Binder.() -> Unit): LifecycleObserver =
    object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun create() {
            bindBlock()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            unbind()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun start() {
            resumed = true
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun stop() {
            resumed = false
        }
    }

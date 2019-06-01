package cat.martori.eventarch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent

internal fun InternalBinder.lifecycleObserver(bindBlock: Binder.() -> Unit): LifecycleObserver =
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

private val lifecycleBinders = mutableMapOf<LifecycleOwner, InternalBinder>()
internal val LifecycleOwner.binder: InternalBinder
    get() = lifecycleBinders[this] ?: InternalBinder().also { lifecycleBinders[this] = it }

package cat.martori.eventarch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope

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

private val lifecycleBinders = mutableMapOf<ViewBinder, InternalBinder>()
internal val ViewBinder.binder: InternalBinder
    get() = lifecycleBinders[this] ?: InternalBinder(lifecycleScope).also { lifecycleBinders[this] = it }

package cat.martori.eventarch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope

internal fun InternalBinder.lifecycleObserver(bindBlock: Binder.() -> Unit): LifecycleObserver =
    object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun bind() {
            bindBlock()
        }
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun unBind() {
            this@lifecycleObserver.unbind()
        }
    }

private val lifecycleBinders = mutableMapOf<ViewBinder, InternalBinder>()
internal val ViewBinder.binder: InternalBinder
    get() = lifecycleBinders[this] ?: InternalBinder(lifecycleScope).also { lifecycleBinders[this] = it }

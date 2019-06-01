package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope


private val scopedBinders = mutableMapOf<CoroutineScope, InternalBinder>()
internal val CoroutineScope.binder: InternalBinder
    get() = scopedBinders[this] ?: InternalBinder(this).apply { binded = true }.also { scopedBinders[this] = it }

package dev.martori.events.coroutines

import dev.martori.events.core.InternalBinder
import kotlinx.coroutines.CoroutineScope


private val scopedBinders = mutableMapOf<CoroutineScope, InternalBinder>()
internal val CoroutineScope.binder: InternalBinder
    get() = scopedBinders[this] ?: InternalBinder(
        this
    ).also { scopedBinders[this] = it }

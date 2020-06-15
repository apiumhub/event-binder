package com.apiumhub.events.coroutines

import com.apiumhub.events.core.InternalBinder
import com.apiumhub.events.core.ReceiverInternal
import com.apiumhub.events.core.internalScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private val scopedBinders = mutableMapOf<CoroutineScope, InternalBinder>()
internal val CoroutineScope.binder: InternalBinder
    get() = scopedBinders[this] ?: InternalBinder(this).also { scopedBinders[this] = it }


internal class SuspendReceiverInternal<T>(private val block: suspend CoroutineScope.(T) -> Unit, scope: CoroutineScope = internalScope) : ReceiverInternal<T>({ scope.launch { block(it) } }) {
    override suspend fun dispatch(value: T) = coroutineScope { block(value) }
}
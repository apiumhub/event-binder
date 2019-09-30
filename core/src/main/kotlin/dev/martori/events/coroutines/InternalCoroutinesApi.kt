package dev.martori.events.coroutines

import dev.martori.events.core.ConsumerInternal
import dev.martori.events.core.InternalBinder
import dev.martori.events.core.internalScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private val scopedBinders = mutableMapOf<CoroutineScope, InternalBinder>()
internal val CoroutineScope.binder: InternalBinder
    get() = scopedBinders[this] ?: InternalBinder(
        this
    ).also { scopedBinders[this] = it }


internal class SuspendConsumerInternal<T>(private val block: suspend CoroutineScope.(T) -> Unit, scope: CoroutineScope = internalScope) : ConsumerInternal<T>({ scope.launch { block(it) } }) {
    override suspend fun dispatch(value: T) = coroutineScope { block(value) }
}
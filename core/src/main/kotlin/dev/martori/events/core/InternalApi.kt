package dev.martori.events.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal val internalScope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder()

internal open class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T> {
    override suspend fun dispatch(value: T) = func(value)
}

internal class CoInEventInternal<T>(private val block: suspend CoroutineScope.(T) -> Unit, scope: CoroutineScope = internalScope) : InEventInternal<T>({ scope.launch { block(it) } }) {
    override suspend fun dispatch(value: T) {
        coroutineScope { block(value) }
    }
}

internal class InternalBinder(private val coroutineScope: CoroutineScope = internalScope) : Binder {

    override fun unbind() {
        jobs.onEach { it.cancel() }.removeAll { !it.isActive }
    }

    private val jobs = mutableListOf<Job>()

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        jobs += flow().onEach { inEvent.func(it) }.launchIn(coroutineScope)
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("viaU")
    override fun <T> OutEvent<T>.via(inEvent: InEvent<Unit>) {
        jobs += flow().onEach { inEvent.func(Unit) }.launchIn(coroutineScope)
    }

    private fun <T> OutEvent<T>.flow() = (this as OutEventInternal<T>).flow
    private fun <T> InEvent<T>.func(data: T) = (this as InEventInternal<T>).func(data)
}

internal class OutEventInternal<T> : OutEvent<T> {

    val flow get() = channel.asFlow().buffer()

    private val channel = BroadcastChannel<T>(CONFLATED)

    override fun invoke(data: T) {
        channel.offer(data)
    }

}
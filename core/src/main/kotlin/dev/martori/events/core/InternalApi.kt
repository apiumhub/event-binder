package dev.martori.events.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal val internalScope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder()

internal open class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T> {
    open suspend fun dispatch(value: T) = func(value)
}

internal class CoInEventInternal<T>(private val block: suspend CoroutineScope.(T) -> Unit, scope: CoroutineScope = internalScope) : InEventInternal<T>({ scope.launch { block(it) } }) {
    override suspend fun dispatch(value: T) = coroutineScope { block(value) }
}

internal class InternalBinder(private val coroutineScope: CoroutineScope = internalScope) : Binder {

    override var resumed = true

    override fun unbind() {
        jobs.onEach { it.cancel() }.removeAll { !it.isActive }
    }

    private val jobs = mutableListOf<Job>()

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        jobs += flow().filter { resumed }.onEach { inEvent.func(it) }.launchIn(coroutineScope)
    }

    override fun <T> OutEvent<T>.viaU(inEvent: InEvent<Unit>) {
        jobs += flow().filter { resumed }.onEach { inEvent.func(Unit) }.launchIn(coroutineScope)
    }

    private fun <T> OutEvent<T>.flow() = (this as OutEventInternal<T>).flow
    private fun <T> InEvent<T>.func(data: T) = (this as InEventInternal<T>).func(data)
}

internal class SingleTimeOutEventInternal<T> : OutEvent<T> {
    private val channel = Channel<T>(CONFLATED)

    override fun invoke(data: T) {
        channel.offer(data)
    }
}

internal class OutEventInternal<T>(retainValue: Boolean = true) : OutEvent<T> {
    private val channel = BroadcastChannel<T>(if (retainValue) CONFLATED else 1)
    val flow = channel.asFlow()

    override fun invoke(data: T) {
        channel.offer(data)
    }
}
package dev.martori.events.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal val internalScope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder()

internal open class ConsumerInternal<T>(val func: (T) -> Unit) : Consumer<T> {
    open suspend fun dispatch(value: T) = func(value)
}

internal class InternalBinder(private val coroutineScope: CoroutineScope = internalScope) : Binder {

    override var resumed = true

    override fun unbind() {
        jobs.onEach { it.cancel() }.removeAll { !it.isActive }
    }

    private val jobs = mutableListOf<Job>()

    override fun <T> Event<T>.via(consumer: Consumer<T>) {
        jobs += flow().filter { resumed }.onEach { consumer.func(it) }.launchIn(coroutineScope)
    }

    override fun <T> Event<T>.viaU(consumer: Consumer<Unit>) {
        jobs += flow().filter { resumed }.onEach { consumer.func(Unit) }.launchIn(coroutineScope)
    }

    private fun <T> Event<T>.flow() = (this as EventInternal<T>).flow
    private fun <T> Consumer<T>.func(data: T) = (this as ConsumerInternal<T>).func(data)
}

internal class SingleTimeEventInternal<T> : Event<T> {
    private val channel = Channel<T>(CONFLATED)

    override fun invoke(data: T) {
        channel.offer(data)
    }
}

internal class EventInternal<T>(retainValue: Boolean = true) : Event<T> {
    private val channel = BroadcastChannel<T>(if (retainValue) CONFLATED else 1)
    val flow = channel.asFlow()

    override fun invoke(data: T) {
        channel.offer(data)
    }
}
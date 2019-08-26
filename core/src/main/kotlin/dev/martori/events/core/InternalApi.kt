package dev.martori.events.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal val internalScope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder()

internal open class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T> {
    override fun dispatch(value: T) = func(value)
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

    override fun <T> InEvent<T>.via(outEvent: OutEvent<T>) {
        outEvent via this@via
    }

    private fun <T> OutEvent<T>.flow() = (this as OutEventInternal<T>).flow
    private suspend fun <T> InEvent<T>.func(data: T) = (this as InEventInternal<T>).func(data)
}

internal class OutEventInternal<T> : OutEvent<T> {

    val flow get() = channel.asFlow().buffer()

    private val channel = BroadcastChannel<T>(CONFLATED)

    override fun invoke(data: T) {
        channel.offer(data)
    }

}
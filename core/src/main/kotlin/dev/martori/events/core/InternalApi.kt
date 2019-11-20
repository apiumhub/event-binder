package dev.martori.events.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.*

internal val internalScope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder()

internal open class ReceiverInternal<T>(val func: (T) -> Unit) : Receiver<T> {
    open suspend fun dispatch(value: T) = func(value)
}

internal class InternalBinder(private val coroutineScope: CoroutineScope = internalScope) : Binder {

    override var resumed = true

    override fun unbind() {
        jobs.onEach { it.cancel() }.removeAll { !it.isActive }
    }

    private val jobs = mutableListOf<Job>()

    override fun <T> Event<T>.via(receiver: Receiver<T>) {
        jobs += flow().filter { resumed }.onEach { receiver.func(it) }.launchIn(coroutineScope)
    }

    override fun <T> Event<T>.viaU(receiver: Receiver<Unit>) {
        jobs += flow().filter { resumed }.onEach { receiver.func(Unit) }.launchIn(coroutineScope)
    }

    private fun <T> Event<T>.flow() = (this as EventInternal<T>).flow //FIXME breaks if SingleTimeEvent
    private fun <T> Receiver<T>.func(data: T) = (this as ReceiverInternal<T>).func(data)
}

internal class SingleTimeEventInternal<T> : Event<T> {
    private val channel = Channel<T>(CONFLATED)

    override fun invoke(data: T) {
        channel.offer(data)
    }

    override fun <R> map(block: (T) -> R): Event<R> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filter(block: (T) -> Boolean): Event<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

internal class EventInternal<T>(private val retainValue: Boolean = true) : Event<T> {
    private val channel = BroadcastChannel<T>(if (retainValue) CONFLATED else 1)
    internal var flow = channel.asFlow()

    override fun invoke(data: T) {
        channel.offer(data)
    }

    override fun <R> map(block: (T) -> R): Event<R> = EventInternal<R>(retainValue).apply {
        this.flow = this@EventInternal.flow.map { block(it) }
    }

    override fun filter(block: (T) -> Boolean): Event<T> = EventInternal<T>(retainValue).apply {
        this.flow = this@EventInternal.flow.filter { block(it) }
    }
}
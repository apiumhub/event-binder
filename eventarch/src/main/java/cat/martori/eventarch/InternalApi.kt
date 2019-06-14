package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal val scope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder(scope)

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>


internal class InternalBinder(private val coroutineScope: CoroutineScope) : Binder {
    internal fun unbind() = jobs.forEach { it.cancel() }

    private val jobs = mutableListOf<Job>()

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        jobs += coroutineScope.launch(Dispatchers.Main) {
            this@via.bindedFlow().collect { inEvent.func(it) }
        }
    }

    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("viaU")
    override fun <T> OutEvent<T>.via(inEvent: InEvent<Unit>) {
        jobs += coroutineScope.launch(Dispatchers.Main) {
            this@via.bindedFlow().collect {
                inEvent.func(Unit)
            }
        }
    }

    override fun <T> InEvent<T>.via(outEvent: OutEvent<T>) {
        outEvent via this@via
    }

    private fun <T> OutEvent<T>.bindedFlow() = (this as OutEventInternal<T>).flow
    private fun <T> InEvent<T>.func(data: T) = (this as InEventInternal<T>).func.invoke(data)

}

internal class OutEventInternal<T>(private val scope: CoroutineScope) : OutEvent<T> {
    val flow get() = channel.asFlow()

    private val channel = BroadcastChannel<T>(CONFLATED)

    override fun invoke(data: T) {
        scope.launch {
            channel.send(data)
        }
    }
}
package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal val scope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder(scope).apply { binded = true }

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>


internal class InternalBinder(private val coroutineScope: CoroutineScope) : Binder {
    override var binded: Boolean = false
        internal set

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        coroutineScope.launch(Dispatchers.Main) {
            this@via.bindedFlow().collect { inEvent.func(it) }
        }
    }

    override fun <T> OutEvent<T>.viaU(inEvent: InEvent<Unit>) {
        coroutineScope.launch(Dispatchers.Main) {
            this@viaU.bindedFlow().collect {
                inEvent.func(Unit)
            }
        }
    }

    override fun <T> InEvent<T>.via(outEvent: OutEvent<T>) {
        outEvent via this@via
    }

    private fun <T> OutEvent<T>.bindedFlow() = (this as OutEventInternal<T>).flow.filter { binded }
    private fun <T> InEvent<T>.func(data: T) = (this as InEventInternal<T>).func.invoke(data)

}

internal class OutEventInternal<T>(private val scope: CoroutineScope) : OutEvent<T> {
    val flow = flow {
        for (value in channel.openSubscription()) {
            emit(value)
        }
    }

    private val channel = BroadcastChannel<T>(BUFFERED)

    override fun invoke(data: T) {
        scope.launch {
            channel.send(data)
        }
    }
}
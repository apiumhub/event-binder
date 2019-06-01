package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal val internalBinder = InternalBinder(GlobalScope).apply { binded = true }

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>

internal class OutEventInternal<T>(val scope: CoroutineScope) : OutEvent<T> {
    val flow = flow<T> {
        emitters.add(this)
    }

    private var emitters = mutableListOf<FlowCollector<T>>()

    override fun invoke(data: T) {
        scope.launch {
            runCatching {
                emitters.forEach {
                    it.emit(data)
                }
            }
        }
    }
}

internal class InternalBinder(private val coroutineScope: CoroutineScope) : Binder {

    override var binded: Boolean = false
        internal set

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        val flow = (this@via as OutEventInternal<T>).flow
        val func = (inEvent as InEventInternal<T>).func
        coroutineScope.launch {
            flow.filter { binded }.collect { func(it) }
        }
    }

    fun <T> newOutEvent(): OutEvent<T> = OutEventInternal(coroutineScope)
}

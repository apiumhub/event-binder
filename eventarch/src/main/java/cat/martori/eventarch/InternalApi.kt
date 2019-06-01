package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal fun getBinder(): Binder = internalBinder

internal val internalBinder = InternalBinder(GlobalScope)

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
        binded = true
        coroutineScope.launch {
            (this@via as OutEventInternal<T>).flow.filter { binded }.collect {
                (inEvent as InEventInternal<T>).func(it)
            }
        }
    }

    fun <T> newOutEvent(): OutEvent<T> = OutEventInternal(coroutineScope)
}

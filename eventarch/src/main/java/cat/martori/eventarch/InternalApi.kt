package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal val scope = CoroutineScope(Dispatchers.Default + Job())

internal val internalBinder = InternalBinder(scope).apply { binded = true }

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>

internal class OutEventInternal<T>(private val scope: CoroutineScope) : OutEvent<T> {
    val flow = flow<T> {
        emitters.add(this)
    }

    private var emitters = mutableListOf<FlowCollector<T>>()

    override fun invoke(data: T) {
        emitters.forEach {
            scope.launch(Dispatchers.Main) {
                it.emit(data)
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
        coroutineScope.launch(Dispatchers.Main) {
            flow.filter { binded }.collect { func(it) }
        }
    }

}


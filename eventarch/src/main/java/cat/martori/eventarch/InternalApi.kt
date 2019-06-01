package cat.martori.eventarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal fun getBinder(): Binder = internalBinder

internal val internalBinder = InternalBinder()

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

internal class InternalBinder(override val coroutineContext: CoroutineContext = Job()) : Binder, CoroutineScope {

    override var binded: Boolean = false
        internal set

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        binded = true
        launch {
            (this@via as OutEventInternal<T>).flow.filter { binded }.collect {
                (inEvent as InEventInternal<T>).func(it)
            }
        }
    }

    fun <T> newOutEvent(): OutEvent<T> = OutEventInternal(this)
}

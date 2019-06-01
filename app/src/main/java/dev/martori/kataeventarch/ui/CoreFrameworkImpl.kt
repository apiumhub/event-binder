package dev.martori.kataeventarch.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


@JvmName("uOutEvent")
fun Bindable.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> Bindable.outEvent(): OutEvent<T> = internalBinder.newOutEvent()

fun Bindable.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = getBinder().apply { bindBlock() }

internal fun getBinder(): Binder = internalBinder

/*naive impls*/
internal val internalBinder = InternalBinder()

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>

internal class OutEventInternal<T>(val scope: CoroutineScope) : OutEvent<T> {
    val flow = flow<T> {
        emitters.add(this)
    }

    private var emitters = mutableListOf<FlowCollector<in T>>()

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

package cat.martori.eventarch

import androidx.lifecycle.LifecycleOwner

typealias ViewBinder = LifecycleOwner


fun <T> ViewBinder.outEvent(): OutEvent<T> =
    newOutEvent() //FIXME this is needed so the events from view and service come from the same binder
fun <T> ViewBinder.inEvent(block: (T) -> Unit): InEvent<T> =
    InEventInternal(block)

@JvmName("extBind")
fun ViewBinder.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.binder?.apply {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: bind(bindBlock)
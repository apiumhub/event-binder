package cat.martori.eventarch

import androidx.lifecycle.LifecycleOwner

typealias ViewBinder = LifecycleOwner

@JvmName("uOutEvent")
fun ViewBinder.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> ViewBinder.outEvent(): OutEvent<T> = binder.newOutEvent()
fun ViewBinder.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> ViewBinder.inEvent(block: (T) -> Unit): InEvent<T> =
    InEventInternal(block)

@JvmName("extBind")
fun ViewBinder.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBinder? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.binder?.apply {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: bind(bindBlock)
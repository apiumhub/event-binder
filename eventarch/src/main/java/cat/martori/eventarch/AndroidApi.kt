package cat.martori.eventarch

import android.arch.lifecycle.LifecycleOwner

typealias ViewBinder = LifecycleOwner

@JvmName("uOutEvent")
fun LifecycleOwner.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> LifecycleOwner.outEvent(): OutEvent<T> = binder.newOutEvent()
fun LifecycleOwner.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> LifecycleOwner.inEvent(block: (T) -> Unit): InEvent<T> =
    InEventInternal(block)

@JvmName("extBind")
fun LifecycleOwner.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: LifecycleOwner? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.binder?.apply {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: bind(bindBlock)
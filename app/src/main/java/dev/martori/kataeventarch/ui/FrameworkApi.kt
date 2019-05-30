package dev.martori.kataeventarch.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent


interface InEvent
interface InDataEvent<T>
interface OutEvent {
    operator fun invoke()
}
interface OutDataEvent<T> {
    operator fun invoke(data: T)
}

interface Binder {
    infix fun <T> OutDataEvent<T>.via(inEvent: InDataEvent<T>)
    infix fun OutEvent.via(inEvent: InEvent)
    fun unBind()
}

fun <T> Bindable.outEvent(): OutDataEvent<T> = TODO()
fun Bindable.outEvent(): OutEvent = TODO()
fun Bindable.inEvent(block: () -> Unit): InEvent = TODO()
fun <T> Bindable.inEvent(block: (T) -> Unit): InDataEvent<T> = TODO()

fun <T> LifecycleOwner.outEvent(): OutDataEvent<T> = TODO()
fun LifecycleOwner.outEvent(): OutEvent = TODO()
fun LifecycleOwner.inEvent(block: () -> Unit): InEvent = TODO()
fun <T> LifecycleOwner.inEvent(block: (T) -> Unit): InDataEvent<T> = TODO()

fun getBinder(): Binder = TODO()

fun getBinder(lifecycleOwner: LifecycleOwner?): Binder = TODO()

fun bind(lifecycleOwner: LifecycleOwner? = null, bindBlock: Binder.() -> Unit): Binder {
    val binder = getBinder(lifecycleOwner)
    if (lifecycleOwner != null) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onCreate() {
                binder.bindBlock()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onDestroy() {
                binder.unBind()
            }
        })
    } else {
        binder.bindBlock()
    }
    return binder
}


interface Bindable
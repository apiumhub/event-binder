package dev.martori.kataeventarch.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent

typealias UInEvent = InEvent<Unit>

interface InEvent<T>

typealias X<T> = InEvent<T>

typealias UOutEvent = OutEvent<Unit>

operator fun UOutEvent.invoke(): Unit = invoke(Unit)

interface OutEvent<T> {
    operator fun invoke(data: T)
}

interface Binder {
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<T>)
    fun unBind()
}

fun <T> Bindable.outEvent(): OutEvent<T> = TODO()
@JvmName("uOutEvent")
fun Bindable.outEvent(): UOutEvent = TODO()

fun Bindable.inEvent(block: () -> Unit): UInEvent = TODO()
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = TODO()

fun <T> LifecycleOwner.outEvent(): OutEvent<T> = TODO()
@JvmName("uOutEvent")
fun LifecycleOwner.outEvent(): UOutEvent = TODO()

fun LifecycleOwner.inEvent(block: () -> Unit): UInEvent = TODO()
fun <T> LifecycleOwner.inEvent(block: (T) -> Unit): InEvent<T> = TODO()

private fun getBinder(lifecycleOwner: LifecycleOwner?): Binder = TODO()

@JvmName("extBind")
fun LifecycleOwner.bind(bindBlock: Binder.() -> Unit) = bind(this, bindBlock)

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
typealias ViewBinder = LifecycleOwner

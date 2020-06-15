package com.apiumhub.events.android

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.apiumhub.events.core.*
import com.apiumhub.events.coroutines.*
import kotlinx.coroutines.CoroutineScope

typealias ViewBindable = LifecycleOwner

fun <T> ViewBindable.event(retainValue: Boolean = true): Event<T> = lifecycleScope.event(retainValue)

@WorkInProgress
fun <T> ViewBindable.singleTimeEvent(): Event<T> = lifecycleScope.singleTimeEvent()
fun <T> ViewBindable.receiver(block: (T) -> Unit): Receiver<T> = lifecycleScope.receiver(block)
fun <T> ViewBindable.suspendReceiver(block: suspend CoroutineScope.(T) -> Unit): Receiver<T> = lifecycleScope.suspendReceiver(block)

//TODO optimize to prevent subscription on each value
fun <T> Fragment.receiver(block: (T) -> Unit): Receiver<T> = lifecycleScope.receiver { data ->
    viewLifecycleOwnerLiveData.observe(this, object : Observer<LifecycleOwner> {
        override fun onChanged(t: LifecycleOwner) {
            t.lifecycleScope.launchWhenCreated { block(data) }
            viewLifecycleOwnerLiveData.removeObserver(this)
        }
    })
}

@JvmName("extBind")
fun ViewBindable.bind(bindBlock: Binder.() -> Unit) =
    bind(this, bindBlock)

fun bind(lifecycleOwner: ViewBindable? = null, bindBlock: Binder.() -> Unit): Binder =
    lifecycleOwner?.lifecycleScope?.bind {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver(bindBlock))
    } ?: GlobalBind.bind(bindBlock)
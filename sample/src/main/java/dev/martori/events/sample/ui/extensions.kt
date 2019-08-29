package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

fun Fragment.whenCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenCreated(block)
}
package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.martori.events.core.Binder
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

fun Fragment.whenCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenCreated(block)
}

inline fun <reified T : Fragment> T.lazyBinds() = inject<Binder>(named<T>()) { parametersOf(this) }
inline fun <reified T : Fragment> T.applyBinds() = get<Binder>(named<T>()) { parametersOf(this) }
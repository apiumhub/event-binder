package dev.martori.events.sample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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

fun Fragment.whenViewCreated(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwnerLiveData.observe(::getLifecycle) {
        it.lifecycleScope.launchWhenCreated(block)
    }
}
inline fun <reified T : Fragment> T.lazyBinds() = inject<Binder>(named<T>()) { parametersOf(this) }
inline fun <reified T : Fragment> T.applyBinds() = get<Binder>(named<T>()) { parametersOf(this) }

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View = LayoutInflater.from(context).inflate(layoutId, this, false)
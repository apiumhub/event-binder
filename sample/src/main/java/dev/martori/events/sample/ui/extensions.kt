package dev.martori.events.sample.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.martori.events.core.Binder
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.inject
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.androidx.scope.lifecycleScope as koinScope

fun Fragment.whenCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenCreated(block)
}

fun Fragment.whenViewCreated(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwnerLiveData.observe(::getLifecycle) {
        it.lifecycleScope.launchWhenCreated(block)
    }
}

inline fun <reified T : Fragment> T.lazyBinds() = inject<Binder>(named<T>()) { parametersOf(this) }
inline fun <reified T : Fragment> T.applyBinds() = koinScope.get<Binder>(named<T>()) { parametersOf(this) }

inline fun <reified T> Module.koinBind(noinline block: Scope.(T) -> Binder) {
    scope<T> {
        scoped(named<T>()) { (bindable: T) ->
            block(bindable)
        }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View = LayoutInflater.from(context).inflate(layoutId, this, false)

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Fragment.Toast(message: CharSequence) = context?.toast(message)
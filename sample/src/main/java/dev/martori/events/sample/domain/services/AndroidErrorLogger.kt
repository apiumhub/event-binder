package dev.martori.events.sample.domain.services

import android.util.Log
import dev.martori.events.core.Receiver
import dev.martori.events.core.receiver
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.binding.views.AsyncView

class AndroidErrorLogger<T> : ErrorLogger<T> {
    override val onError: Receiver<AsyncView<T>> = receiver {
        if (it is AsyncView.Error) Log.e("ErrorLogger", "unkownError", it.reason)
    }
}
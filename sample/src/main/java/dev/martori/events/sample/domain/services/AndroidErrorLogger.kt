package dev.martori.events.sample.domain.services

import android.util.Log
import dev.martori.events.core.Receiver
import dev.martori.events.core.receiver
import dev.martori.events.sample.binding.services.ErrorLogger
import dev.martori.events.sample.binding.views.AsyncView

class AndroidErrorLogger : ErrorLogger {
    override val onAsyncError: Receiver<AsyncView.Error<*>> = receiver {
        Log.e("ErrorLogger", "unkownError", it.reason)
    }
    override val onError: Receiver<Error> = receiver {
        Log.e("ErrorLogger", "unkownError", it)
    }
}
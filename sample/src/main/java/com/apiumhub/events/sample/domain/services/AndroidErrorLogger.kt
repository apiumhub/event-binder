package com.apiumhub.events.sample.domain.services

import android.util.Log
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.core.receiver
import com.apiumhub.events.sample.binding.services.ErrorLogger

class AndroidErrorLogger : ErrorLogger {
    override val onError: Receiver<Error> = receiver {
        Log.e("ErrorLogger", "unkownError", it)
    }
}
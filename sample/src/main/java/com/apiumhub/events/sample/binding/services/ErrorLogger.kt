package com.apiumhub.events.sample.binding.services

import com.apiumhub.events.core.Bindable
import com.apiumhub.events.core.Receiver

interface ErrorLogger : Bindable {
    val onError: Receiver<Error>
}
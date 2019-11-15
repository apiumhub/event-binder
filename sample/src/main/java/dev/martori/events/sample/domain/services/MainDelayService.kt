package dev.martori.events.sample.domain.services

import dev.martori.events.core.Receiver
import dev.martori.events.core.Event
import dev.martori.events.core.receiver
import dev.martori.events.core.event
import dev.martori.events.sample.binding.services.MainService
import dev.martori.events.sample.ui.LibTestFragment.Companion.LEFT
import dev.martori.events.sample.ui.LibTestFragment.Companion.RIGHT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainDelayService : MainService {

    val scope = CoroutineScope(Dispatchers.Main)

    override val requestTextById: Receiver<Int> = receiver { id ->
        scope.launch {
            delay(2000)
            sendText(calculateText(id))
        }
    }

    private fun calculateText(id: Int) = when (id) {
        LEFT -> "Left"
        RIGHT -> "Right"
        else -> "Other"
    }

    override val sendText: Event<String> = event()
}
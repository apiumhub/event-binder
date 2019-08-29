package dev.martori.events.sample.domain.services

import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.core.inEvent
import dev.martori.events.core.outEvent
import dev.martori.events.sample.binding.services.MainService
import dev.martori.events.sample.ui.MainActivity.Companion.LEFT
import dev.martori.events.sample.ui.MainActivity.Companion.RIGHT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainDelayService : MainService {

    val scope = CoroutineScope(Dispatchers.Main)

    override val requestTextById: InEvent<Int> = inEvent { id ->
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

    override val sendText: OutEvent<String> = outEvent()
}
package dev.martori.kataeventarch.domain

import dev.martori.eventarch.InEvent
import dev.martori.eventarch.OutEvent
import dev.martori.eventarch.inEvent
import dev.martori.eventarch.outEvent
import dev.martori.kataeventarch.binding.MainService
import dev.martori.kataeventarch.ui.MainActivity.Companion.LEFT
import dev.martori.kataeventarch.ui.MainActivity.Companion.RIGHT
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
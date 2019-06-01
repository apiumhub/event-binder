package dev.martori.kataeventarch.domain

import cat.martori.eventarch.InEvent
import cat.martori.eventarch.OutEvent
import cat.martori.eventarch.inEvent
import cat.martori.eventarch.outEvent
import dev.martori.kataeventarch.binding.MainService
import dev.martori.kataeventarch.ui.MainActivity.Companion.LEFT
import dev.martori.kataeventarch.ui.MainActivity.Companion.RIGHT
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainDelayService : MainService {
    override val requestTextById: InEvent<Int> = inEvent { id ->
        runBlocking {
            delay(500)
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
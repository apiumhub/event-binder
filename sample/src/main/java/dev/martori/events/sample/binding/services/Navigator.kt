package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU

interface Navigator : Bindable {
    val openDetails: Receiver<Int>
    val openList: ReceiverU
}
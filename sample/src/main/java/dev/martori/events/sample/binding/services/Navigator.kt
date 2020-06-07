package dev.martori.events.sample.binding.services

import dev.martori.events.core.Bindable
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.domain.entities.Id

interface Navigator : Bindable {
    val openDetails: Receiver<Id>
    val openList: ReceiverU
}
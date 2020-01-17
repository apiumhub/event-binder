package dev.martori.events.sample.binding.views

import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.sample.domain.entities.Details

interface DetailView : AsyncModelView<Details> {
    val goBack: EventU
    val loadDetails: Event<Int>
}
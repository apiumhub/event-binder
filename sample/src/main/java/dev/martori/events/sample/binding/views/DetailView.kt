package dev.martori.events.sample.binding.views

import dev.martori.events.core.Event
import dev.martori.events.core.EventU

interface DetailView : AsyncModelView<DetailViewModel> {
    val goBack: EventU
    val loadDetails: Event<Int>
}

data class DetailViewModel(val id: Int, val name: String)
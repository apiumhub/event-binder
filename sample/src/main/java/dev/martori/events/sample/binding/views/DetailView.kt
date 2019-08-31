package dev.martori.events.sample.binding.views

import dev.martori.events.core.OutEvent
import dev.martori.events.core.OutEventU

interface DetailView : AsyncModelView<DetailViewModel> {
    val goBack: OutEventU
    val loadDetails: OutEvent<Int>
}

data class DetailViewModel(val id: Int, val name: String)
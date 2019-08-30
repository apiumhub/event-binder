package dev.martori.events.sample.data.inmemory

import dev.martori.events.core.*
import dev.martori.events.sample.binding.services.DetailsService
import dev.martori.events.sample.binding.views.DetailViewModel

class InMemoryDetailsService : DetailsService {

    override val modelLoaded: OutEvent<DetailViewModel> = outEvent()

    override val loadDetails: InEvent<Int> = inEvent {
        modelLoaded(DetailViewModel(it, "I'm $it"))
    }
    override val error: OutEvent<Error>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val startProcess: OutEventU
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
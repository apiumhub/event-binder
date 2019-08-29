package dev.martori.events.sample.binding.views

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.sample.binding.services.CounterService


interface MainView {
    val clickButton: OutEvent<Int>
    val showText: InEvent<String>
    val showCounter: InEvent<Int>
}

fun ViewBindable.bindMainViewCounterService(view: MainView, service: CounterService) = bind {
    service.totalCount via view.showCounter
    view.clickButton via service.modifyCounter
}
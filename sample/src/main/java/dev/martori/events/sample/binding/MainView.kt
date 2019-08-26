package dev.martori.events.sample.binding

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent


interface MainView {
    val clickButton: OutEvent<Int>
    val showText: InEvent<String>
    val showCounter: InEvent<Int>
}

fun ViewBindable.bindMainViewMainService(view: MainView, service: MainService) = bind {
    service.sendText via view.showText
    view.clickButton via service.requestTextById
}

fun ViewBindable.bindMainViewCounterService(view: MainView, service: CounterService) = bind {
    service.totalCount via view.showCounter
    view.clickButton via service.modifyCounter
}
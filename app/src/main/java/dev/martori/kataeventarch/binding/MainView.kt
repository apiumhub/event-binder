package dev.martori.kataeventarch.binding

import cat.martori.core.InEvent
import cat.martori.core.OutEvent
import dev.martori.eventarch.ViewBinder
import dev.martori.eventarch.bind

interface MainView {
    val clickButton: OutEvent<Int>
    val showText: InEvent<String>
    val showCounter: InEvent<Int>
}

fun ViewBinder.bindMainViewMainService(view: MainView, service: MainService) = bind {
    service.sendText via view.showText
    view.clickButton via service.requestTextById
}

fun ViewBinder.bindMainViewCounterService(view: MainView, service: CounterService) = bind {
    service.totalCount via view.showCounter
    view.clickButton via service.modifyCounter
}
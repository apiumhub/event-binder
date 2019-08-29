package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.CounterService
import dev.martori.events.sample.binding.services.MainService
import dev.martori.events.sample.binding.views.MainView

fun ViewBindable.bindMainViewMainService(view: MainView, service: MainService) = bind {
    service.sendText via view.showText
    view.clickButton via service.requestTextById
}

fun ViewBindable.bindMainViewCounterService(view: MainView, service: CounterService) = bind {
    service.totalCount via view.showCounter
    view.clickButton via service.modifyCounter
}
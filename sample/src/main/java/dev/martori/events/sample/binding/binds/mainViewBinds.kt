package dev.martori.events.sample.binding.views

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.MainService

fun ViewBindable.bindMainViewMainService(view: MainView, service: MainService) = bind {
    service.sendText via view.showText
    view.clickButton via service.requestTextById
}
package dev.martori.kataeventarch.binding

import cat.martori.eventarch.InEvent
import cat.martori.eventarch.OutEvent
import cat.martori.eventarch.ViewBinder
import cat.martori.eventarch.bind

interface MainView {
    val clickButton: OutEvent<Int>
    val showText: InEvent<String>
}

fun ViewBinder.bindMainViewMainService(view: MainView, service: MainService) = bind {
    service.sendText via view.showText
    view.clickButton via service.requestTextById
}
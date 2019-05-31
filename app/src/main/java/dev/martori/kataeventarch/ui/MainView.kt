package dev.martori.kataeventarch.ui

import android.support.v4.app.Fragment

fun main() {
    val view = MainViewFrag()
    val service = MainServiceImple()
    view.viewServiceBinder(view, service)
}

interface MainView {
    val showLoading: UInEvent
    val showData: InEvent<String>
    val clickView: OutEvent<Int>
}

interface MainService : Bindable {
    val requestById: InEvent<Int>
    val startedWork: UOutEvent
    val receivedData: OutEvent<String>
}

fun get(): MainService = TODO()

class MainViewFrag : Fragment(), MainView {

    val x = viewServiceBinder(this, get())

    override val clickView = outEvent<Int>()

    override val showLoading = inEvent {
        println("Loading...")
    }

    override val showData = inEvent { data: String ->
        println("data: $data")
    }

    fun click() {
        clickView(1)
    }
}

class MainServiceImple : MainService {
    override val requestById = inEvent { id: Int ->
        startedWork()
        receivedData("$id")
    }
    override val startedWork = outEvent()
    override val receivedData = outEvent<String>()
}

fun ViewBinder.viewServiceBinder(view: MainView, service: MainService) = bind {
    view.clickView via service.requestById
    service.receivedData via view.showData
    service.startedWork via view.showLoading
}
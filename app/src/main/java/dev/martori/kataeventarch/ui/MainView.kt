package dev.martori.kataeventarch.ui

fun main() {
    val view = MainViewFrag()
    val service = MainServiceImple()
    val service2 = MainServiceImple()

    viewServiceBinder(view, service)
    viewServiceBinder(view, service2)

    view.click()
}

interface MainView : Bindable {
    val showLoading: UInEvent
    val showData: InEvent<String>
    val clickView: OutEvent<Int>
}

interface MainService : Bindable {
    val requestById: InEvent<Int>
    val startedWork: UOutEvent
    val receivedData: OutEvent<String>
}

class MainViewFrag : MainView {

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

fun viewServiceBinder(view: MainView, service: MainService) = bind {
    view.clickView via service.requestById
    service.receivedData via view.showData
    service.startedWork via view.showLoading
}
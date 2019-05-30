package dev.martori.kataeventarch.ui

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment

fun main() {
    val view = MainViewFrag()
    val service = MainServiceImple()
    viewServiceBinder(view, service)
}

interface MainView : LifecycleOwner {
    val showLoading: InEvent
    val showData: InDataEvent<String>
    val clickView: OutDataEvent<Int>
}

interface MainService : Bindable {
    val requestById: InDataEvent<Int>
    val startedWork: OutEvent
    val receivedData: OutDataEvent<String>
}

class MainViewFrag : Fragment(), MainView {

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

fun viewServiceBinder(view: MainView, service: MainService) = bind(view) {
    view.clickView via service.requestById
    service.receivedData via view.showData
    service.startedWork via view.showLoading
}
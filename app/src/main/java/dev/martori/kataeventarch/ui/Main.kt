package dev.martori.kataeventarch.ui

//
//fun main() {
//    val view = MainViewFrag()
//    val service = MainServiceImple("A")
//    val service2 = MainServiceImple("B")
//
//    runBlocking {
//        viewServiceBinder(view, service)
//        viewServiceBinder(view, service2)
//
//        delay(1000)
//        view.click(1)
//        delay(100)
//        view.click(2)
//        delay(1000)
//    }
//    runBlocking {
//
//        delay(1000)
//        view.click(3)
//        delay(100)
//        view.click(4)
//        delay(1000)
//    }
//}
//
//interface MainView : Bindable {
//    val showLoading: UInEvent
//    val showData: InEvent<String>
//    val clickView: OutEvent<Int>
//}
//
//interface MainService : Bindable {
//    val requestById: InEvent<Int>
//    val startedWork: UOutEvent
//    val receivedData: OutEvent<String>
//}
//
//class MainViewFrag : MainView {
//
//    override val clickView = outEvent<Int>()
//
//    override val showLoading = inEvent {
//        println("Loading...")
//    }
//
//    override val showData = inEvent { data: String ->
//        println("data: $data")
//    }
//
//    fun click(i: Int) {
//        clickView(i)
//    }
//}
//
//class MainServiceImple(val prefix: String) : MainService {
//    override val requestById = inEvent { id: Int ->
//        startedWork()
//        GlobalScope.launch {
//            delay(200)
//            receivedData("$prefix $id")
//        }
//    }
//    override val startedWork = outEvent()
//    override val receivedData = outEvent<String>()
//}
//
//fun viewServiceBinder(view: MainView, service: MainService) = bind {
//    view.clickView via service.requestById
//    service.receivedData via view.showData
//    service.startedWork via view.showLoading
//}
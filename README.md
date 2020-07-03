# How do I use It?
## Declare your interfaces
```kotlin
interface DataListView {
    val requestList: Event<DataListRequest>
    val onError: Receiver<Error>
    val onLoading: ReceiverU
    val displayList: Receiver<List<Data>>
}

interface DataListService {
    val loadData: Receiver<DataListRequest>
    val errorReceived: Event<Error>
    val startedFetching: EventU
    val dataListReceived: Event<List<Data>>
}
```
The `Event<T>` tipe defines an action this interface can perform on another. In this example, DataListView can request a list.
The `Receiver<T>` tipe defines an action this interfaces can receive. In this example DataListView can receive and Error.
The `EventU` and `ReceiverU` are aliases for events and receivers that don't need to send or receive a parameter
## Bind them
```kotlin
fun ViewBindable.bindAnimeList(view: DataListView, service: dataListService) = bind {
    view.displayList via service.dataListReceived
    view.onError via service.errorReceived
    view.onLoading via service.startedFetching
    view.requestList via service.loadData
}
```
We can define a function on how this interfaces comunicate binding the emission of events with the corresponding receivers.
The function is defined as an extension of:
* ViewBindable: if we want the binding to live in the lifeCycle of an android component (for example: Fragments).
* CoBindable: if we want the binding to live in the scope of a coroutine.
* Bindable: if we want the binds to live for the whole application live.

## Implement the interfaces
```kotlin
class NetworkDataListService(repository: NetworkDataRepository) : DataListService, Bindable {
    override val errorReceived: Event<Error> = event(retainValue = false)
    override val startedFetching: EventU = event(retainValue = false)
    override val dataListReceived: Event<List<Data>> = event()
    override val loadData: Receiver<DataListRequest> = suspendReceiver {
        startedFetching()
        runCatching { repository.getList(it) }.fold({
            dataListReceived(it)
        }, {
            errorReceived(Error(it))
        })
    }
}
```
The service must implement Bindable in order to be able to create event and receiver implementations. Android LifeCycleOwners (for example: Activities) don't need to do this as extensions for them are provided out of the box.

We implement our events, as they are outbound actions no implementation is required, and the `Bindable.event` method will handle it for us.
The optional parameter `retainValue` is used to specify if we want the lastest data sent to be received by new subscriptors. In this case, we want new binded views to display the last available data, but we don't want the last error to be displayed every time a view is binded with this service.

Then we implement the receiver. `receiver` and `suspendReceiver` are provided, suspendReceiver provides a CoroutineScope with the same lifeCycle as the binds, so coroutines are fully suported. Inside the receiver we can call the events as if the were methods of the class which will call the receivers binded to them.

# How do I test my code?
A fluent test API is provided to ease the testing process.
### We can test that when an Event is received another one is dispatched
```kotlin
    @Test
    fun `load data dispatches fetch and error events if request fails`() {
        val error = Error()
        coEvery { repository.getList(any()) } throws error

        sut.loadData withParameter request shouldDispatch {
            sut.startedFetching withParameter Unit
            sut.errorReceived withType Error::class
            sut.errorReceived assertOverParameter {
                assertEquals(error, it.cause)
            }
        }
    }
```
several methods such as `withAny Paramenter`, `withType` or `never Dispatched` are provided.
### We can manualy dispatch events to verify the behaviour of other components
```kotlin
        val request = DataListRequest()
        (sut.loadData withParameter request).dispatch()
        coVerify { repository.getList(request) }
```
### We can create new binds just for testing pourposes 
the method `testBind` is provided so that test binds can be created and executed in a test environment.
For example
```kotlin
    @Test
    fun `test bind example`() = testBind {
        sut.errorReceived via receiver {
            //this will be called when errorReceived is called
        }
        (sut.loadData withParameter request).dispatch()
    }
```

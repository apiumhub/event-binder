package cat.martori.eventarch

interface Bindable

interface InEvent<T>
typealias UInEvent = InEvent<Unit>

interface OutEvent<T> {
    operator fun invoke(data: T)
}
typealias UOutEvent = OutEvent<Unit>

operator fun UOutEvent.invoke(): Unit = invoke(Unit)

interface Binder {
    infix fun <T> OutEvent<T>.via(inEvent: InEvent<T>)
    val binded: Boolean
}

@JvmName("uOutEvent")
fun Bindable.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> Bindable.outEvent(): OutEvent<T> = internalBinder.newOutEvent()

fun Bindable.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> =
    InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = internalBinder.apply { bindBlock() }

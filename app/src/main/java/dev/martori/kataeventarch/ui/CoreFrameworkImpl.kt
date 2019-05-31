package dev.martori.kataeventarch.ui


@JvmName("uOutEvent")
fun Bindable.outEvent(): UOutEvent = outEvent<Unit>()

fun <T> Bindable.outEvent(): OutEvent<T> = internalBinder.newOutEvent()

fun Bindable.inEvent(block: () -> Unit): UInEvent = inEvent<Unit> { block() }
fun <T> Bindable.inEvent(block: (T) -> Unit): InEvent<T> = InEventInternal(block)

fun bind(bindBlock: Binder.() -> Unit): Binder = getBinder().apply { bindBlock() }

internal fun getBinder(): Binder = internalBinder

/*naive impls*/
internal val internalBinder = InternalBinder()

internal class InEventInternal<T>(val func: (T) -> Unit) : InEvent<T>

internal class InternalBinder : Binder {

    val binds = mutableMapOf<OutEvent<*>, InEvent<*>>()

    override fun <T> OutEvent<T>.via(inEvent: InEvent<T>) {
        binds[this] = inEvent
    }

    override fun unBind() {
        binds.clear()
    }

    fun <T> newOutEvent(): OutEvent<T> = object :
        OutEvent<T> {
        override fun invoke(data: T) {
            val ine = binds[this]
            ine?.runCatching {
                (this as InEventInternal<T>).func(data)
            }
        }
    }
}
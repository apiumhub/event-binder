package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.core.Receiver
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailView
import dev.martori.events.sample.domain.entities.Details
import dev.martori.events.sample.ui.models.toViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details), DetailView {
    private val detailsId
        get() = arguments?.getInt("id") ?: throw IllegalStateException("MissingArgument")

    init {
        whenCreated {
            applyBinds()
            loadDetails(detailsId)
        }
    }

    override val loadDetails: Event<Int> = event()
    override val goBack: EventU = event()
    override val renderState: Receiver<AsyncView<Details>> = receiver {
        when (it) {
            is AsyncView.Success -> {
                stateTextView.text = "Name: ${it.model.toViewModel().name}"
            }
            is AsyncView.Loading -> {
                stateTextView.text = "Loading..."
            }
            is AsyncView.Error -> {
                stateTextView.text = "Error!"
            }
        }
    }
}
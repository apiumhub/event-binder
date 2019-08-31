package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import dev.martori.events.android.inEvent
import dev.martori.events.android.outEvent
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU
import dev.martori.events.core.OutEvent
import dev.martori.events.core.OutEventU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.DetailView
import dev.martori.events.sample.binding.views.DetailViewModel
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

    override val loadDetails: OutEvent<Int> = outEvent()
    override val goBack: OutEventU = outEvent()

    override val renderLoading: InEventU = inEvent {
        stateTextView.text = "Loading..."
    }
    override val renderModel: InEvent<DetailViewModel> = inEvent {
        stateTextView.text = "Name: ${it.name}"
    }
    override val renderError: InEvent<Error> = inEvent {
        stateTextView.text = "Error!"
    }

}
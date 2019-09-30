package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.consumer
import dev.martori.events.android.event
import dev.martori.events.core.Consumer
import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.AsyncView
import dev.martori.events.sample.binding.views.DetailView
import dev.martori.events.sample.binding.views.DetailViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(R.layout.fragment_details), DetailView {
    private val detailsId
        get() = arguments?.getInt("id") ?: throw IllegalStateException("MissingArgument")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinds()
        if (savedInstanceState == null) {
            loadDetails(detailsId)
        }
    }

    override val loadDetails: Event<Int> = event()
    override val goBack: EventU = event()
    override val renderState: Consumer<AsyncView<DetailViewModel>> = consumer {
        when (it) {
            is AsyncView.Success -> {
                stateTextView.text = "Name: ${it.model.name}"
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
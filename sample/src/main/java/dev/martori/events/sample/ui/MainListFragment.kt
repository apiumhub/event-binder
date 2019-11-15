package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import dev.martori.events.android.receiver
import dev.martori.events.android.event
import dev.martori.events.core.Receiver
import dev.martori.events.core.Event
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.MainListView
import dev.martori.events.sample.domain.entities.ListElement
import dev.martori.events.sample.ui.adapters.MainListAdapter
import kotlinx.android.synthetic.main.fragment_main_list.*

class MainListFragment : Fragment(R.layout.fragment_main_list), MainListView {
    override val openDetails: Event<Int> = event()
    override val requestListElements: Event<Int> = event()
    private val adapter = MainListAdapter {
        openDetails(it.id)
    }

    override val showListElements: Receiver<List<ListElement>> = receiver {
        adapter.elements = it
    }

    init {
        whenCreated {
            applyBinds()
            requestListElements(0)
        }
        whenViewCreated {
            mainList.adapter = adapter
        }
    }
}
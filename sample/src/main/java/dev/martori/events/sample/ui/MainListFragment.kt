package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.consumer
import dev.martori.events.android.event
import dev.martori.events.core.Consumer
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

    override val showListElements: Consumer<List<ListElement>> = consumer {
        adapter.elements = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainList.adapter = adapter
        if (savedInstanceState == null) {
            applyBinds()
            requestListElements(0)
        }
    }
}
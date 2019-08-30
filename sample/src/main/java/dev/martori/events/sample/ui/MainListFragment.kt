package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.outEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.binds.bindListNavigation
import dev.martori.events.sample.binding.views.MainListView
import kotlinx.android.synthetic.main.fragment_main_list.*
import org.koin.android.ext.android.get

class MainListFragment : Fragment(R.layout.fragment_main_list), MainListView {

    init {
        whenCreated {
            bindListNavigation(this@MainListFragment, get())
        }
    }

    override val openDetails: OutEvent<Int> = outEvent()

    private val currentId: Int?
        get() = idInputText.text.toString().toIntOrNull()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openDetailsButton.setOnClickListener {
            openDetailsWithId()
        }
        idInputText.setOnEditorActionListener { _, _, _ ->
            openDetailsWithId()
            true
        }
    }

    private fun openDetailsWithId() {
        currentId?.let { openDetails(it) }
    }
}
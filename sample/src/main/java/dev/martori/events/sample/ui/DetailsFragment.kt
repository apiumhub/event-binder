package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.outEvent
import dev.martori.events.core.OutEventU
import dev.martori.events.core.invoke
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.binds.bindDetailsNavigation
import dev.martori.events.sample.binding.views.DetailView
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.ext.android.get

class DetailsFragment : Fragment(R.layout.fragment_details), DetailView {
    init {
        whenCreated {
            bindDetailsNavigation(this@DetailsFragment, get())
        }
    }

    override val goBack: OutEventU = outEvent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBackButton.setOnClickListener { goBack() }
    }
}
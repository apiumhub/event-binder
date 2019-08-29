package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.inEvent
import dev.martori.events.android.outEvent
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.binds.bindMainViewCounterService
import dev.martori.events.sample.binding.binds.bindMainViewMainService
import dev.martori.events.sample.binding.views.MainView
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.get

class MainFragment : Fragment(R.layout.fragment_main), MainView {

    override val clickButton: OutEvent<Int> = outEvent()
    override val showText: InEvent<String> = inEvent {
        tvMainText.text = it
    }
    override val showCounter: InEvent<Int> = inEvent {
        tvCount.text = "clicked: $it"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindMainViewMainService(this@MainFragment, get())
        bindMainViewCounterService(this@MainFragment, get())
        btnLeft.setOnClickListener { clickButton(LEFT) }
        btnRight.setOnClickListener { clickButton(RIGHT) }
    }

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }
}


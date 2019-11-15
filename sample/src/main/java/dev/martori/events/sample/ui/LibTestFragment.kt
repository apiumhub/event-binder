package dev.martori.events.sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.martori.events.android.receiver
import dev.martori.events.android.event
import dev.martori.events.core.Receiver
import dev.martori.events.core.Event
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.binds.bindMainViewCounterService
import dev.martori.events.sample.binding.binds.bindMainViewMainService
import dev.martori.events.sample.binding.views.LibTestView
import kotlinx.android.synthetic.main.fragment_lib_test.*
import org.koin.android.ext.android.get

class LibTestFragment : Fragment(R.layout.fragment_lib_test), LibTestView {

    override val clickButton: Event<Int> = event()
    override val showText: Receiver<String> = receiver {
        tvMainText.text = it
    }
    override val showCounter: Receiver<Int> = receiver {
        tvCount.text = "clicked: $it"
    }

    init {
        whenCreated {
            bindMainViewMainService(this@LibTestFragment, get())
            bindMainViewCounterService(this@LibTestFragment, get())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLeft.setOnClickListener { clickButton(LEFT) }
        btnRight.setOnClickListener { clickButton(RIGHT) }
    }

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }
}


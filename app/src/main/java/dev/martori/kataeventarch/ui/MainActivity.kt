package dev.martori.kataeventarch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cat.martori.eventarch.InEvent
import cat.martori.eventarch.OutEvent
import cat.martori.eventarch.inEvent
import cat.martori.eventarch.outEvent
import dev.martori.kataeventarch.R
import dev.martori.kataeventarch.binding.MainView
import dev.martori.kataeventarch.binding.bindMainViewCounterService
import dev.martori.kataeventarch.binding.bindMainViewMainService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity(), MainView {
    override val showCounter: InEvent<Int> = inEvent {
        tvCount.text = "clicked: $it"
    }

    init {
        lifecycleScope.launchWhenCreated {
            bindMainViewMainService(this@MainActivity, get())
            bindMainViewCounterService(this@MainActivity, get())
            btnLeft.setOnClickListener { clickButton(LEFT) }
            btnRight.setOnClickListener { clickButton(RIGHT) }
        }
    }

    override val clickButton: OutEvent<Int> = outEvent()
    override val showText: InEvent<String> = inEvent {
        tvMainText.text = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }
}

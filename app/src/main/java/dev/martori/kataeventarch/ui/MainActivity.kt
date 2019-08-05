package dev.martori.kataeventarch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cat.martori.core.InEvent
import cat.martori.core.OutEvent
import dev.martori.eventarch.inEvent
import dev.martori.eventarch.outEvent
import dev.martori.kataeventarch.R
import dev.martori.kataeventarch.binding.MainView
import dev.martori.kataeventarch.binding.bindMainViewCounterService
import dev.martori.kataeventarch.binding.bindMainViewMainService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity(), MainView {
    override val clickButton: OutEvent<Int> = outEvent()

    override val showCounter: InEvent<Int> = inEvent {
        tvCount.text = "clicked: $it"
    }
    override val showText: InEvent<String> = inEvent {
        tvMainText.text = it
    }

    init {
        lifecycleScope.launchWhenCreated {
            bindMainViewMainService(this@MainActivity, get())
            bindMainViewCounterService(this@MainActivity, get())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLeft.setOnClickListener { clickButton(LEFT) }
        btnRight.setOnClickListener { clickButton(RIGHT) }
    }

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }
}
package dev.martori.kataeventarch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    override val clickButton: OutEvent<Int> = outEvent()

    override val showCounter: InEvent<Int> = inEvent {
        tvCount.text = "clicked: $it"
    }
    override val showText: InEvent<String> = inEvent {
        tvMainText.text = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindMainViewMainService(this, get())
        bindMainViewCounterService(this, get())
        btnLeft.setOnClickListener { clickButton(LEFT) }
        btnRight.setOnClickListener { clickButton(RIGHT) }
    }

    companion object {
        const val LEFT = 1
        const val RIGHT = 2
    }
}
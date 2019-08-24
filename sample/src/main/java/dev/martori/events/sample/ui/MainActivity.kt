package dev.martori.events.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.martori.events.android.inEvent
import dev.martori.events.android.outEvent
import dev.martori.events.core.InEvent
import dev.martori.events.core.OutEvent
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.MainView
import dev.martori.events.sample.binding.bindMainViewCounterService
import dev.martori.events.sample.binding.bindMainViewMainService
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

interface Destination {
    val name: String
}

sealed class LoginDestination(override val name: String) : Destination {
    object Register : LoginDestination("Register")
    object Login : LoginDestination("Login")
}

sealed class MainDestination(override val name: String) : Destination {
    object Main : MainDestination("Main")
}


interface Navigator<T : Destination> {
    fun navigate(destination: T)
}

interface LoginNavigator : Navigator<LoginDestination>
interface MainNavigator : Navigator<MainDestination>

class AndroidLoginNavigator : LoginNavigator {
    override fun navigate(destination: LoginDestination) = when (destination) {
        LoginDestination.Register -> TODO()
        LoginDestination.Login -> TODO()
    }
}

fun main() {
    val login = AndroidLoginNavigator()
    login.navigate(LoginDestination.Login)
}
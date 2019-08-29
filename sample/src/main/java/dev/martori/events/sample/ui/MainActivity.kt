package dev.martori.events.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.services.Navigator
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mainModule = module {
        single<Navigator> { NavHostNavigator(navHostFragment.findNavController()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(mainModule)
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(mainModule)
    }
}
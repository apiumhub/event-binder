package dev.martori.events.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.services.Navigator
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navController
        get() = navHostFragment.findNavController()

    private val mainModule = module {
        single<Navigator> { NavHostNavigator(navController) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBarWithNavController(navController)
        loadKoinModules(mainModule)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(mainModule)
    }
}
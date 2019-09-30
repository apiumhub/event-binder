package dev.martori.events.sample.ui

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import dev.martori.events.core.Consumer
import dev.martori.events.core.ConsumerU
import dev.martori.events.core.consumer
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.services.Navigator


class NavHostNavigator(private val navController: NavController) : Navigator {
    override val openDetails: Consumer<Int> = consumer {
        navController.navigate(
            R.id.details, bundleOf(
                "id" to it //todo migrate to safe args
            )
        )
    }
    override val openList: ConsumerU = consumer {
        navController.navigateUp()
    }
}
package dev.martori.events.sample.ui

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import dev.martori.events.core.InEvent
import dev.martori.events.core.InEventU
import dev.martori.events.core.inEvent
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.services.Navigator


class NavHostNavigator(private val navController: NavController) : Navigator {
    override val openDetails: InEvent<Int> = inEvent {
        navController.navigate(
            R.id.details, bundleOf(
                "id" to it //todo migrate to safe args
            )
        )
    }
    override val openList: InEventU = inEvent {
        navController.navigateUp()
    }
}
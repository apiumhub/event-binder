package dev.martori.events.sample.ui

import androidx.navigation.NavController
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.core.receiver
import dev.martori.events.sample.binding.services.Navigator
import dev.martori.events.sample.domain.entities.Id


class NavHostNavigator(private val navController: NavController) : Navigator {
    override val openDetails: Receiver<Id> = receiver {
        navController.navigate(AnimeListFragmentDirections.actionListToDetails(it))
    }
    override val openList: ReceiverU = receiver {
        navController.navigateUp()
    }
}
package dev.martori.events.sample.ui

import androidx.navigation.NavController
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.core.receiver
import dev.martori.events.sample.binding.services.Navigator


class NavHostNavigator(private val navController: NavController) : Navigator {
    override val openDetails: Receiver<Int> = receiver {
        //        navController.navigate(
//            R.id.details, bundleOf(
//                "id" to it //todo migrate to safe args
//            )
//        )
    }
    override val openList: ReceiverU = receiver {
        navController.navigateUp()
    }
}
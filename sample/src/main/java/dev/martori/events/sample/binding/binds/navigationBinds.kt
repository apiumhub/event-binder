package dev.martori.events.sample.binding.binds

import dev.martori.events.android.ViewBindable
import dev.martori.events.android.bind
import dev.martori.events.sample.binding.services.Navigator
import dev.martori.events.sample.binding.views.DetailView
import dev.martori.events.sample.binding.views.MainListView

fun ViewBindable.bindListNavigation(mainListView: MainListView, navigator: Navigator) = bind {
    mainListView.openDetails via navigator.openDetails
}

fun ViewBindable.bindDetailsNavigation(detailsView: DetailView, navigator: Navigator) = bind {
    detailsView.goBack via navigator.openList
}
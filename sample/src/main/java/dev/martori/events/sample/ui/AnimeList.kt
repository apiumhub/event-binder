package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.AnimeListView
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.ui.adapters.AnimeListAdapter
import kotlinx.android.synthetic.main.fragment_main_list.*

class AnimeList : Fragment(R.layout.fragment_main_list), AnimeListView {
    private val adapter = AnimeListAdapter {}

    override val requestAnimeByYear: Event<Int> = event()
    override val onError: Receiver<Error> = receiver {
        context?.toast("Error: ${it.message}")
    }
    override val onLoading: ReceiverU = receiver {
        context?.toast("Loading...")
    }
    override val displayAnimeList: Receiver<List<Anime>> = receiver {

    }

    init {
        whenCreated {
            applyBinds()
            requestAnimeByYear(2020)
        }
        whenViewCreated {
            mainList.adapter = adapter
        }
    }
}
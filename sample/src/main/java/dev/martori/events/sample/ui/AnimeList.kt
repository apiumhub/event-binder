package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.binding.views.AnimeListView
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.ui.adapters.AnimeListAdapter
import dev.martori.events.sample.ui.models.toViewModel
import kotlinx.android.synthetic.main.fragment_main_list.*

class AnimeList : Fragment(R.layout.fragment_main_list), AnimeListView {

    private val adapter = AnimeListAdapter {
        requestAnime(AnimeRequest(it))
    }

    override val requestAnime: Event<AnimeRequest> = event()
    override val onError: Receiver<Error> = receiver {
        Toast("Error: ${it.message}")
    }
    override val onLoading: ReceiverU = receiver {
        Toast("Loading...")
    }
    override val displayAnimeList: Receiver<List<Anime>> = receiver { list ->
        adapter.elements = (adapter.elements + list.map { it.toViewModel() }).distinct()
    }

    init {
        whenCreated {
            applyBinds()
            requestAnime(AnimeRequest())
        }
        whenViewCreated {
            mainList.adapter = adapter
        }
    }
}
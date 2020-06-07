package dev.martori.events.sample.ui

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.models.AnimeListRequest
import dev.martori.events.sample.binding.views.AnimeListView
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.ui.adapters.AnimeListAdapter
import dev.martori.events.sample.ui.models.toViewModel
import kotlinx.android.synthetic.main.fragment_main_list.*

class AnimeListFragment : Fragment(R.layout.fragment_main_list), AnimeListView {

    private val adapter = AnimeListAdapter(
        endReached = {
            requestAnime(AnimeListRequest(it))
        }, clickListener = {
            findNavController().navigate(AnimeListFragmentDirections.actionListToDetails(it))
        }
    )
    override val requestAnime: Event<AnimeListRequest> = event()
    override val onError: Receiver<Error> = receiver {
        mainProgress.isVisible = false
        Toast("Error: ${it.message}")
    }
    override val onLoading: ReceiverU = receiver {
        mainProgress.isVisible = true
    }
    override val displayAnimeList: Receiver<List<Anime>> = receiver { list ->
        mainProgress.isVisible = false
        adapter.elements = (adapter.elements + list.map { it.toViewModel() }).distinct()
    }

    init {
        applyBinds()
        /*
        * TODO find a way to prevent calling this on orientation changes
        *  the solution is no on a single time event since it will be recreated with the fragment
        * */
        requestAnime(AnimeListRequest())

        whenViewCreated {
            mainList.adapter = adapter
        }
    }
}
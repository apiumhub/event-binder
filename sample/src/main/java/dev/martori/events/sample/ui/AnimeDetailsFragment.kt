package dev.martori.events.sample.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.Receiver
import dev.martori.events.core.ReceiverU
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.AnimeDetailsView
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id
import kotlinx.android.synthetic.main.fragment_details.*

class AnimeDetailsFragment : Fragment(R.layout.fragment_details), AnimeDetailsView {
    override val requestAnimeDetails: Event<Id> = event()
    override val displayAnime: Receiver<Anime> = receiver {
        (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
        animeDetailsCover.load(it.coverImage.uri)
        animeDetailsSynopsis.text = it.synopsis
    }
    override val displayError: Receiver<Error> = receiver {
        Toast("Error: ${it.message}")
    }
    override val displayLoading: ReceiverU = receiver {
        Toast("Loading...")
    }

    private val args: AnimeDetailsFragmentArgs by navArgs()

    init {
        whenViewCreated {
            applyBinds()
            requestAnimeDetails(args.animeId)
        }
    }
}
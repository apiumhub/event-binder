package dev.martori.events.sample.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dev.martori.events.android.event
import dev.martori.events.android.receiver
import dev.martori.events.core.Event
import dev.martori.events.core.EventU
import dev.martori.events.core.Receiver
import dev.martori.events.sample.R
import dev.martori.events.sample.binding.views.AnimeDetailsView
import dev.martori.events.sample.domain.entities.Anime
import dev.martori.events.sample.domain.entities.Id
import kotlinx.android.synthetic.main.fragment_details.*

class AnimeDetailsFragment : Fragment(R.layout.fragment_details), AnimeDetailsView {
    override val requestAnimeDetails: Event<Id> = event()
    override val displayAnime: Receiver<Anime> = receiver {
        sampleText.text = it.id.id
    }
    override val goBack: EventU = event()
    private val args: AnimeDetailsFragmentArgs by navArgs()

    init {
        whenCreated {
            applyBinds()
            requestAnimeDetails(args.animeId)
        }
    }
}
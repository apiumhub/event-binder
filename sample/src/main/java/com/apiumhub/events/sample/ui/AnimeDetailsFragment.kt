package com.apiumhub.events.sample.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.apiumhub.events.android.event
import com.apiumhub.events.android.receiver
import com.apiumhub.events.core.Event
import com.apiumhub.events.core.Receiver
import com.apiumhub.events.core.ReceiverU
import com.apiumhub.events.sample.R
import com.apiumhub.events.sample.binding.views.AnimeDetailsView
import com.apiumhub.events.sample.domain.entities.Anime
import com.apiumhub.events.sample.domain.entities.Id
import kotlinx.android.synthetic.main.fragment_details.*

class AnimeDetailsFragment : Fragment(R.layout.fragment_details), AnimeDetailsView {
    override val requestAnimeDetails: Event<Id> = event()
    override val displayAnime: Receiver<Anime> = receiver {
        animeDetailsProgress.isVisible = false
        (activity as? AppCompatActivity)?.supportActionBar?.title = it.name
        animeDetailsCover.load(it.coverImage.uri) {
            placeholder(R.drawable.placeholder)
        }
        animeDetailsSynopsis.text = it.synopsis
    }
    override val displayError: Receiver<Error> = receiver {
        animeDetailsProgress.isVisible = false
        Toast("Error: ${it.message}")
    }
    override val displayLoading: ReceiverU = receiver {
        animeDetailsProgress.isVisible = true
    }

    private val args: AnimeDetailsFragmentArgs by navArgs()

    init {
        applyBinds()
        whenViewCreated {
            requestAnimeDetails(args.animeId)
        }
    }
}
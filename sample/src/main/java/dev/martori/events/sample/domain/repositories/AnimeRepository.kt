package dev.martori.events.sample.domain.repositories

import dev.martori.events.sample.binding.models.AnimeRequest
import dev.martori.events.sample.domain.entities.Anime

interface AnimeRepository : Repository<AnimeRequest, List<Anime>>
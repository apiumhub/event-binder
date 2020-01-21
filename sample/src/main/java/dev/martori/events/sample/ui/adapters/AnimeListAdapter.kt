package dev.martori.events.sample.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.martori.events.sample.R
import dev.martori.events.sample.ui.inflate
import dev.martori.events.sample.ui.models.AnimeViewModel
import kotlinx.android.synthetic.main.item_anime_list.view.*
import kotlin.properties.Delegates

class AnimeListAdapter(private val endReached: (count: Int) -> Unit, private val clickListener: (id: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var elements: List<AnimeViewModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerView.ViewHolder(parent.inflate(R.layout.item_anime_list)) {}

    override fun getItemCount(): Int = elements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val anime = elements[position]
        holder.itemView.itemName.text = anime.name
        if (position >= itemCount - 1) endReached(itemCount)
        holder.itemView.setOnClickListener { clickListener(anime.id) }
    }
}

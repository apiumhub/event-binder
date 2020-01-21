package dev.martori.events.sample.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.martori.events.sample.R
import dev.martori.events.sample.ui.inflate
import dev.martori.events.sample.ui.models.AnimeViewModel
import kotlinx.android.synthetic.main.item_main_list.view.*
import kotlin.properties.Delegates

class AnimeListAdapter(private val endReached: (count: Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var elements: List<AnimeViewModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_list)) {}

    override fun getItemCount(): Int = elements.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val name = elements[position].name
        holder.itemView.itemId.text = "0"
        holder.itemView.itemName.text = name
        if (position >= itemCount - 1) endReached(itemCount)
    }
}

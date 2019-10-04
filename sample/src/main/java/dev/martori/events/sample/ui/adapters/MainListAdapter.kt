package dev.martori.events.sample.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.martori.events.sample.R
import dev.martori.events.sample.domain.entities.ListElement
import dev.martori.events.sample.ui.inflate
import kotlinx.android.synthetic.main.item_main_list.view.*
import kotlin.properties.Delegates

class MainListAdapter : RecyclerView.Adapter<Holder>() {

    var elements: List<ListElement> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(parent.inflate(R.layout.item_main_list))

    override fun getItemCount(): Int = elements.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val (id, name) = elements[position]
        holder.itemView.itemId.text = id.toString()
        holder.itemView.itemName.text = name
    }
}

class Holder(view: View) : RecyclerView.ViewHolder(view)
package ge.gmegrelishvili.todoapp.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.ListItem

class ListItemAdapter : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    private val listItems = mutableListOf<ListItem>()

    inner class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ListItem) {}
    }

    fun addListItem(item: ListItem) {
        listItems.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}
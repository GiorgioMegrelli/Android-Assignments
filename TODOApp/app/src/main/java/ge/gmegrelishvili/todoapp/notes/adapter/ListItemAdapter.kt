package ge.gmegrelishvili.todoapp.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.ListItem

class ListItemAdapter : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    private val listItems = mutableListOf<ListItem>()

    inner class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItem) {
            view.findViewById<CheckBox>(R.id.check_box).isChecked = item.checked
            view.findViewById<EditText>(R.id.todo_item_value).setText(item.value ?: "")

            val editText = view.findViewById<EditText>(R.id.todo_item_value)
            val closeButton = view.findViewById<ImageView>(R.id.close_todo_item)
            editText.setOnFocusChangeListener { _, hasFocus ->
                closeButton.visibility = if (hasFocus) ImageView.VISIBLE else ImageView.INVISIBLE
            }
            closeButton.setOnClickListener {}
        }
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
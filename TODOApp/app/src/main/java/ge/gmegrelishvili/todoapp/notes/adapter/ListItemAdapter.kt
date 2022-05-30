package ge.gmegrelishvili.todoapp.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R

class ListItemAdapter(private val noteActivityInvoker: NoteActivityInvoker) :
    RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    private val listItems = mutableListOf<ListItemStored>()

    data class ListItemStored(
        val localId: Int,
        val id: Int?,
        var checked: Boolean,
        var value: String,
    ) {
        var checkedSrc: CheckBox? = null
        var valueSrc: EditText? = null

        fun selfUpdate() {
            if (checkedSrc != null) {
                checked = checkedSrc!!.isChecked
            }
            if (valueSrc != null) {
                value = valueSrc!!.text.toString()
            }
        }
    }

    inner class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItemStored) {
            val checkBox = view.findViewById<CheckBox>(R.id.check_box)
            val editText = view.findViewById<EditText>(R.id.todo_item_value)
            val closeButton = view.findViewById<ImageView>(R.id.close_todo_item)

            item.checkedSrc = checkBox
            item.valueSrc = editText

            checkBox.isChecked = item.checked
            editText.setText(item.value)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                noteActivityInvoker.checked(item.localId, isChecked)
            }

            editText.setOnFocusChangeListener { _, hasFocus ->
                closeButton.visibility = if (hasFocus) ImageView.VISIBLE else ImageView.INVISIBLE
            }
            closeButton.setOnClickListener {
                noteActivityInvoker.remove(item.localId)
            }
        }
    }

    fun addListItem(localId: Int, id: Int, checked: Boolean, value: String) {
        val itemStored = ListItemStored(localId, id, checked, value)
        listItems.add(itemStored)
    }

    fun addListItem(localId: Int, checked: Boolean, value: String) {
        val itemStored = ListItemStored(localId, null, checked, value)
        listItems.add(itemStored)
    }

    fun clear() {
        listItems.clear()
    }

    fun remove(localId: Int): ListItemStored? {
        val filtered = listItems.filter {
            it.localId == localId
        }
        return if (filtered.isEmpty()) {
            null
        } else {
            filtered[0]
        }
    }

    fun getAll(): List<ListItemStored> {
        listItems.forEach {
            it.selfUpdate()
        }
        return listItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        listItems.sortBy {
            it.localId
        }
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
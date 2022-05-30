package ge.gmegrelishvili.todoapp.notes.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ListItemAdapterLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}
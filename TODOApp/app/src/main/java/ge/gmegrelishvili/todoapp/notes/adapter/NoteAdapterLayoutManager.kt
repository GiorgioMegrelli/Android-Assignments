package ge.gmegrelishvili.todoapp.notes.adapter

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NoteAdapterLayoutManager(context: Context, columnN: Int) :
    GridLayoutManager(context, columnN) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}
package ge.gmegrelishvili.todoapp.notes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems
import ge.gmegrelishvili.todoapp.notes.NoteActivity
import kotlin.math.min

class MainActivityNoteAdapter(private val parentActivity: AppCompatActivity) :
    RecyclerView.Adapter<MainActivityNoteAdapter.NoteViewHolder>() {

    private val pinnedNotes = mutableListOf<NoteWithListItems>()
    private val unpinnedNotes = mutableListOf<NoteWithListItems>()

    private val resources = parentActivity.resources
    private val applicationContext = parentActivity.applicationContext

    private val maxVisibleItems = 3

    inner class NoteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(note: NoteWithListItems) {
            val checkCount = checkedNotes(note)
            val checkCountStr = resources.getString(R.string.checked_items_count).format(checkCount)

            view.findViewById<TextView>(R.id.note_title).text = note.note.title
            if (checkCount > 0) {
                val bottomText = view.findViewById<TextView>(R.id.bottom_text)
                bottomText.text = checkCountStr
                bottomText.visibility = TextView.VISIBLE
            }
            if (note.listItems.size > maxVisibleItems) {
                view.findViewById<TextView>(R.id.view_dots).visibility = TextView.VISIBLE
            }

            view.setOnClickListener {
                val noteActivity = Intent(parentActivity, NoteActivity::class.java)
                noteActivity.putExtra(NoteActivity.IdResultKey, note.note.id.toString())
                parentActivity.startActivity(noteActivity)
            }

            val inflater =
                applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val firstListItems = view.findViewById<LinearLayout>(R.id.first_list_items)
            val firstItems =
                note.listItems.slice(0 until min(note.listItems.size, maxVisibleItems))

            for (item in firstItems) {
                val view = inflater.inflate(R.layout.fragment_note_item, null)
                view.findViewById<CheckBox>(R.id.check_box).isChecked = item.checked
                view.findViewById<TextView>(R.id.value).text = item.value
                firstListItems.addView(view)
            }
        }

        private fun checkedNotes(note: NoteWithListItems): Int {
            var result = 0
            for (listItem in note.listItems) {
                if (listItem.checked) result++
            }
            return result
        }
    }

    fun addNote(note: NoteWithListItems) {
        unpinnedNotes.add(note)
    }

    fun clear() {
        unpinnedNotes.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(unpinnedNotes[position])
    }

    override fun getItemCount(): Int {
        return pinnedNotes.size + unpinnedNotes.size
    }
}
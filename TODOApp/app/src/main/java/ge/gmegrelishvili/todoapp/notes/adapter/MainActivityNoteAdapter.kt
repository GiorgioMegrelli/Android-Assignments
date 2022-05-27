package ge.gmegrelishvili.todoapp.notes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems
import ge.gmegrelishvili.todoapp.notes.NoteActivity

class MainActivityNoteAdapter(private val parentActivity: AppCompatActivity) :
    RecyclerView.Adapter<MainActivityNoteAdapter.NoteViewHolder>() {

    private val pinnedNotes = mutableListOf<NoteWithListItems>()
    private val unpinnedNotes = mutableListOf<NoteWithListItems>()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: NoteWithListItems) {
            itemView.findViewById<TextView>(R.id.note_title).text = note.note.title
            itemView.findViewById<TextView>(R.id.bottom_text).text = note.note.id.toString()

            itemView.setOnClickListener {
                val noteActivity = Intent(parentActivity, NoteActivity::class.java)
                noteActivity.putExtra(NoteActivity.IdResultKey, note.note.id.toString())
                parentActivity.startActivity(noteActivity)
            }
        }
    }

    fun addNote(note: NoteWithListItems) {
        unpinnedNotes.add(note)
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
package ge.gmegrelishvili.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems
import ge.gmegrelishvili.todoapp.notes.NoteActivity
import ge.gmegrelishvili.todoapp.notes.NoteViewModel
import ge.gmegrelishvili.todoapp.notes.adapter.MainActivityNoteAdapter
import ge.gmegrelishvili.todoapp.notes.adapter.NoteAdapterLayoutManager

class MainActivity : AppCompatActivity() {

    private val columnNumber = 2
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this, NoteViewModel.Companion.NoteViewModelFactory(applicationContext)
        ).get(NoteViewModel::class.java)
    }

    private lateinit var pinnedAdapter: MainActivityNoteAdapter
    private lateinit var otherAdapter: MainActivityNoteAdapter

    private lateinit var pinnedRecyclerView: RecyclerView
    private lateinit var otherRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.addTextChangedListener {
            drawNotes()
        }

        findViewById<FloatingActionButton>(R.id.create_note_button).setOnClickListener {
            val noteActivity = Intent(applicationContext, NoteActivity::class.java)
            noteActivity.putExtra(NoteActivity.IdResultKey, NoteActivity.NoId)
            startActivity(noteActivity)
        }

        setUpAdapters()
        drawNotes()
    }

    private fun setUpAdapters() {
        pinnedAdapter = MainActivityNoteAdapter(this)
        otherAdapter = MainActivityNoteAdapter(this)

        pinnedRecyclerView = findViewById(R.id.recycler_notes_pinned)
        otherRecyclerView = findViewById(R.id.recycler_notes_other)

        pinnedRecyclerView.adapter = pinnedAdapter
        otherRecyclerView.adapter = otherAdapter

        pinnedRecyclerView.layoutManager = NoteAdapterLayoutManager(this, columnNumber)
        pinnedRecyclerView.addItemDecoration(NotesGridLayoutDecoration())
        otherRecyclerView.layoutManager = NoteAdapterLayoutManager(this, columnNumber)
        otherRecyclerView.addItemDecoration(NotesGridLayoutDecoration())
    }

    private fun drawNotes() {
        val searchText = findViewById<EditText>(R.id.search_edit_text).text.toString()

        val recyclerPinnedText = findViewById<TextView>(R.id.recycler_notes_pinned_text)
        val recyclerOtherText = findViewById<TextView>(R.id.recycler_notes_other_text)
        val recyclerPinned = findViewById<RecyclerView>(R.id.recycler_notes_pinned)

        Thread {
            val notes = viewModel.getWLINotes(searchText)
            val hasPinned = hasPinnedNotes(notes)

            runOnUiThread {
                recyclerPinnedText.visibility = toVisibility(hasPinned)
                recyclerOtherText.visibility = toVisibility(hasPinned)
                recyclerPinned.visibility = toVisibility(hasPinned)

                pinnedAdapter.clear()
                otherAdapter.clear()

                for (note in notes) {
                    if (hasPinned && note.note.isPinned) {
                        pinnedAdapter.addNote(note)
                    } else {
                        otherAdapter.addNote(note)
                    }
                }

                notifyDataSetChanged()
            }
        }.start()
    }

    private fun toVisibility(hasPinned: Boolean): Int {
        return if (hasPinned) TextView.VISIBLE else TextView.GONE
    }

    private fun hasPinnedNotes(notes: List<NoteWithListItems>): Boolean {
        for (note in notes) {
            if (note.note.isPinned) {
                return true
            }
        }
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataSetChanged() {
        pinnedRecyclerView.post {
            pinnedAdapter.notifyDataSetChanged()
        }
        otherRecyclerView.post {
            otherAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        drawNotes()
    }

    inner class NotesGridLayoutDecoration : RecyclerView.ItemDecoration() {

        private val gap = resources.getDimension(R.dimen.grid_note_between_margin)

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position < columnNumber - 1) {
                outRect.right = gap.toInt()
            }
        }
    }
}
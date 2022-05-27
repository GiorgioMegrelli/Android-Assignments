package ge.gmegrelishvili.todoapp

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.gmegrelishvili.todoapp.database.TodoDatabase
import ge.gmegrelishvili.todoapp.notes.NoteActivity
import ge.gmegrelishvili.todoapp.notes.NoteViewModel
import ge.gmegrelishvili.todoapp.notes.adapter.MainActivityNoteAdapter

class MainActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this,
            NoteViewModel.Companion.NoteViewModelFactory(applicationContext)
        ).get(NoteViewModel::class.java)
    }
    private val columnNumber = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TodoDatabase.getInstance(applicationContext)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.addTextChangedListener {}

        findViewById<FloatingActionButton>(R.id.create_note_button).setOnClickListener {
            val noteActivity = Intent(applicationContext, NoteActivity::class.java)
            noteActivity.putExtra(NoteActivity.IdResultKey, NoteActivity.NoId)
            startActivity(noteActivity)
        }

        drawNotes()
    }

    private fun drawNotes() {
        val adapter = MainActivityNoteAdapter(this)
        val recyclerNotes = findViewById<RecyclerView>(R.id.recycler_notes)
        val gapSize = resources.getDimension(R.dimen.grid_note_between_margin)
        recyclerNotes.adapter = adapter
        recyclerNotes.layoutManager = GridLayoutManager(this, columnNumber)
        recyclerNotes.addItemDecoration(NotesGridLayoutDecoration(gapSize))

        Thread {
            val notes = TodoDatabase.getInstance(this).getNoteDao().getWLINotes()

            runOnUiThread {
                for (note in notes) {
                    adapter.addNote(note)
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    inner class NotesGridLayoutDecoration(private val gap: Float) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position < columnNumber - 1) {
                outRect.right = gap.toInt()
            }
        }
    }
}
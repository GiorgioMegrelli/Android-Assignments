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
import ge.gmegrelishvili.todoapp.notes.NoteActivity
import ge.gmegrelishvili.todoapp.notes.NoteViewModel
import ge.gmegrelishvili.todoapp.notes.adapter.MainActivityNoteAdapter

class MainActivity : AppCompatActivity() {

    private val columnNumber = 2
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this, NoteViewModel.Companion.NoteViewModelFactory(applicationContext)
        ).get(NoteViewModel::class.java)
    }

    private lateinit var adapter: MainActivityNoteAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.addTextChangedListener {}

        findViewById<FloatingActionButton>(R.id.create_note_button).setOnClickListener {
            val noteActivity = Intent(applicationContext, NoteActivity::class.java)
            noteActivity.putExtra(NoteActivity.IdResultKey, NoteActivity.NoId)
            startActivity(noteActivity)
        }

        setUpAdapters()
        drawNotes()
    }

    private fun setUpAdapters() {
        adapter = MainActivityNoteAdapter(this)
        recyclerView = findViewById(R.id.recycler_notes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, columnNumber)
        recyclerView.addItemDecoration(NotesGridLayoutDecoration())
    }

    private fun drawNotes() {
        val searchText = findViewById<EditText>(R.id.search_edit_text).text.toString()

        Thread {
            val notes = viewModel.getWLINotes(searchText)

            runOnUiThread {
                adapter.clear()
                for (note in notes) {
                    adapter.addNote(note)
                }
                adapter.notifyDataSetChanged()
            }
        }.start()
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

    companion object {
        const val UpdateResultKey = "UPDATE_RESULT_KEY"
    }
}
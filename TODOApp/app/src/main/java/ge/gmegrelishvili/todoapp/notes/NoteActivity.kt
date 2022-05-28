package ge.gmegrelishvili.todoapp.notes

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.ListItem
import ge.gmegrelishvili.todoapp.notes.adapter.ListItemAdapter


class NoteActivity : AppCompatActivity() {

    private var noteObjectId: Int? = null

    private var isPinned = false
    private val listItems = mutableListOf<ListItem>()

    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this, NoteViewModel.Companion.NoteViewModelFactory(applicationContext)
        ).get(NoteViewModel::class.java)
    }

    private val initialIds = mutableListOf<Int>()
    private val removedIds = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        noteObjectId = parseInt(intent.getStringExtra(IdResultKey))
        if (noteObjectId != null) {
            Thread {
                val note = viewModel.getWLINote(noteObjectId!!)
                listItems.addAll(note.listItems)
                runOnUiThread {
                    findViewById<EditText>(R.id.note_page_title).setText(note.note.title)
                    setPinImage()
                }
                setUpAdapters()
            }.start()
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.pin_button).setOnClickListener {
            isPinned = !isPinned
            setPinImage()
        }
    }

    private fun setUpAdapters() {
        val uncheckedItemsAdapter = ListItemAdapter()
        val checkedItemsAdapter = ListItemAdapter()

        val recyclerViewUnchecked = findViewById<RecyclerView>(R.id.recycler_view_unchecked)
        val recyclerViewChecked = findViewById<RecyclerView>(R.id.recycler_view_checked)

        recyclerViewUnchecked.adapter = uncheckedItemsAdapter
        recyclerViewChecked.adapter = checkedItemsAdapter
        recyclerViewUnchecked.layoutManager = ListItemAdapterLayoutManager()
        recyclerViewChecked.layoutManager = ListItemAdapterLayoutManager()

        for (listItem in listItems) {
            if (listItem.checked) {
                checkedItemsAdapter.addListItem(listItem)
            } else {
                uncheckedItemsAdapter.addListItem(listItem)
            }
            initialIds.add(listItem.id)
        }

        uncheckedItemsAdapter.notifyDataSetChanged()
        checkedItemsAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val titleValue = findViewById<EditText>(R.id.note_page_title).text.toString()
        if (noteObjectId == null) {
            viewModel.insert(titleValue, isPinned)
        } else {
            viewModel.update(noteObjectId!!, titleValue, isPinned)
        }
        finish()
    }

    private fun setPinImage() {
        val currImage = if (isPinned) R.drawable.ic_pinned else R.drawable.ic_pin
        findViewById<ImageView>(R.id.pin_button).setImageDrawable(
            ContextCompat.getDrawable(this, currImage)
        )
    }

    private fun parseInt(str: String?): Int? {
        if (str == NoId) {
            return null
        }
        return try {
            str?.toInt()
        } catch (e: Exception) {
            null
        }
    }

    inner class ListItemAdapterLayoutManager : LinearLayoutManager(this) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }

    companion object {
        const val IdResultKey = "ID_RESULT_KEY"
        const val NoId = ""
    }
}
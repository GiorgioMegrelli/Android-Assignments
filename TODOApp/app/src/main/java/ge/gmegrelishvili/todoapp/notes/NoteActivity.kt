package ge.gmegrelishvili.todoapp.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.todoapp.R
import ge.gmegrelishvili.todoapp.database.entity.ListItem
import ge.gmegrelishvili.todoapp.notes.adapter.ListItemAdapter
import ge.gmegrelishvili.todoapp.notes.adapter.ListItemAdapterLayoutManager
import ge.gmegrelishvili.todoapp.notes.adapter.NoteActivityInvoker
import ge.gmegrelishvili.todoapp.notes.notehelper.IndexCounter
import ge.gmegrelishvili.todoapp.notes.notehelper.MutablePair
import ge.gmegrelishvili.todoapp.notes.notehelper.MutableTriple
import ge.gmegrelishvili.todoapp.notes.notehelper.NumberUtils


class NoteActivity : AppCompatActivity(), NoteActivityInvoker {

    private var noteObjectId: Int? = null

    private var isPinned = false

    private val nextLocalId = IndexCounter()
    private val localIdListItems = sortedMapOf<Int, ListItem>()
    private val localIdNewListItems = sortedMapOf<Int, MutablePair<Boolean, String?>>()
    private val removedLocalOldIds = hashSetOf<Int>()

    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(
            this, NoteViewModel.Companion.NoteViewModelFactory(applicationContext)
        ).get(NoteViewModel::class.java)
    }

    private lateinit var recyclerViewUnchecked: RecyclerView
    private lateinit var recyclerViewChecked: RecyclerView

    private lateinit var uncheckedItemsAdapter: ListItemAdapter
    private lateinit var checkedItemsAdapter: ListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        setUpAdapters()

        noteObjectId = NumberUtils.parseInt(intent.getStringExtra(IdResultKey))
        if (noteObjectId != null) {
            Thread {
                val note = viewModel.getWLINote(noteObjectId!!)
                isPinned = note.note.isPinned
                for (item in note.listItems) {
                    localIdListItems[nextLocalId.nextIndex()] = item
                }
                runOnUiThread {
                    findViewById<EditText>(R.id.note_page_title).setText(note.note.title)
                    setPinImage()
                    drawListItems()
                }
            }.start()
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            onBackPressed()
        }
        findViewById<ImageView>(R.id.pin_button).setOnClickListener {
            isPinned = !isPinned
            setPinImage()
        }
        findViewById<ConstraintLayout>(R.id.add_item_list).setOnClickListener {
            val nextId = nextLocalId.nextIndex()
            localIdNewListItems[nextId] = MutablePair(false, null)
            uncheckedItemsAdapter.addListItem(nextId, false, "")
            notifyDataSetChanged()
        }
    }

    private fun setUpAdapters() {
        recyclerViewUnchecked = findViewById(R.id.recycler_view_unchecked)
        recyclerViewChecked = findViewById(R.id.recycler_view_checked)

        uncheckedItemsAdapter = ListItemAdapter(this)
        checkedItemsAdapter = ListItemAdapter(this)

        recyclerViewUnchecked.adapter = uncheckedItemsAdapter
        recyclerViewChecked.adapter = checkedItemsAdapter
        recyclerViewUnchecked.layoutManager = ListItemAdapterLayoutManager(this)
        recyclerViewChecked.layoutManager = ListItemAdapterLayoutManager(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataSetChanged() {
        recyclerViewUnchecked.post {
            uncheckedItemsAdapter.notifyDataSetChanged()
        }
        recyclerViewChecked.post {
            checkedItemsAdapter.notifyDataSetChanged()
        }
    }

    private fun drawListItems() {
        uncheckedItemsAdapter.clear()
        checkedItemsAdapter.clear()

        val itemMap = sortedMapOf<Int, MutableTriple<Int?, Boolean, String?>>()
        localIdListItems.forEach { (key, value) ->
            itemMap[key] = MutableTriple(value.id, value.checked, value.value)
        }
        localIdNewListItems.forEach { (key, value) ->
            itemMap[key] = MutableTriple(null, value.first, value.second)
        }

        itemMap.forEach { (key, value) ->
            if (value.first == null) {
                if (value.second) {
                    checkedItemsAdapter
                } else {
                    uncheckedItemsAdapter
                }.addListItem(key, value.second, value.third ?: "")
            } else {
                if (value.second) {
                    checkedItemsAdapter
                } else {
                    uncheckedItemsAdapter
                }.addListItem(
                    key, value.first!!, value.second, value.third ?: ""
                )
            }
        }

        notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val titleValue = findViewById<EditText>(R.id.note_page_title).text.toString()
        if (noteObjectId == null) {
            val newListItems = mutableListOf<Pair<Boolean, String?>>()
            localIdNewListItems.forEach { (_, value) ->
                newListItems.add(Pair(value.first, value.second))
            }
            viewModel.insert(titleValue, isPinned, newListItems)
        } else {
            val newListItems = mutableListOf<Pair<String?, Boolean>>()
            localIdNewListItems.forEach { (_, value) ->
                newListItems.add(Pair(value.second, value.first))
            }
            viewModel.update(
                noteObjectId!!, titleValue, isPinned,
                getModifiedListItems(), newListItems, removedLocalOldIds
            )
        }
    }

    private fun getModifiedListItems(): List<ListItem> {
        if (noteObjectId == null) {
            return listOf()
        }
        val oldIds = HashSet<Int>(localIdListItems.values.map { it.id })
        val result = mutableListOf<ListItem>()
        for (stored in checkedItemsAdapter.getAll() + uncheckedItemsAdapter.getAll()) {
            if (stored.id != null && oldIds.contains(stored.id)) {
                result.add(ListItem(stored.id, noteObjectId!!, stored.checked, stored.value))
            }
        }
        return result
    }

    private fun setPinImage() {
        val currImage = if (isPinned) R.drawable.ic_pinned else R.drawable.ic_pin
        findViewById<ImageView>(R.id.pin_button).setImageDrawable(
            ContextCompat.getDrawable(this, currImage)
        )
    }

    override fun checked(localId: Int, isChecked: Boolean) {
        /*
        if (localIdListItems[localId] != null) {
            localIdListItems[localId]!!.checked = isChecked
        } else if (localIdNewListItems[localId] != null) {
            localIdNewListItems[localId]!!.first = isChecked
        }
        drawListItems()
        */
    }

    override fun remove(localId: Int) {
        val oldRemoved = localIdListItems.remove(localId)
        val newRemoved = localIdNewListItems.remove(localId)
        if (oldRemoved != null || newRemoved != null) {
            if (oldRemoved != null) {
                removedLocalOldIds.add(oldRemoved.id)
            }
            drawListItems()
        }
    }

    companion object {
        const val IdResultKey = "ID_RESULT_KEY"
        const val NoId = ""
    }
}
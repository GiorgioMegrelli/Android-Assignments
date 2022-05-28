package ge.gmegrelishvili.todoapp.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.todoapp.database.TodoDatabase
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    fun getWLINotes(searchText: String? = null): List<NoteWithListItems> {
        return if (searchText == null || searchText.isEmpty()) {
            repository.getWLINotes()
        } else {
            repository.getWLINotes(searchText)
        }
    }

    fun getWLINote(id: Int): NoteWithListItems {
        return repository.getWLINote(id)
    }

    fun insert(
        title: String?,
        isPinned: Boolean,
        listItems: List<Pair<String, Boolean>> = listOf()
    ) {
        Thread {
            repository.insertNote(title, isPinned, listItems)
        }.start()
    }

    fun update(
        id: Int,
        title: String?,
        isPinned: Boolean,
        listItems: List<Pair<String, Boolean>> = listOf(),
        newListItems: List<Pair<String, Boolean>> = listOf(),
        deleteListItems: List<Pair<String, Boolean>> = listOf(),
    ) {
        Thread {
            repository.updateNote(id, title, isPinned)
        }.start()
    }

    companion object {
        const val ExceptionString = "Illegal ViewModel"

        class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                    return NoteViewModel(
                        NoteRepository(
                            TodoDatabase.getInstance(context).getNoteDao(),
                            TodoDatabase.getInstance(context).getListItemDao(),
                        )
                    ) as T
                }
                throw IllegalStateException(ExceptionString)
            }
        }
    }
}
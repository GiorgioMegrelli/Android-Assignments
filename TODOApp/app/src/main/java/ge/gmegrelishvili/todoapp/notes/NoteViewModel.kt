package ge.gmegrelishvili.todoapp.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ge.gmegrelishvili.todoapp.database.TodoDatabase

class NoteViewModel(repository: NoteRepository) : ViewModel() {
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
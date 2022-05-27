package ge.gmegrelishvili.todoapp.notes.model

import ge.gmegrelishvili.todoapp.database.entity.ListItem
import ge.gmegrelishvili.todoapp.database.entity.Note

data class NoteModel(
    val id: Int,
    val title: String?,
    val isPinned: Boolean,
    val todoItems: List<ListItemModel> = mutableListOf(),
) {
    fun addTodoItem(todoItem: ListItem) {}

    companion object {
        fun from(note: Note): NoteModel {
            return NoteModel(note.id, note.title, note.isPinned)
        }
    }
}

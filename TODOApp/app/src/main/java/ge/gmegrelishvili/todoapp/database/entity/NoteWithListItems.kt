package ge.gmegrelishvili.todoapp.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithListItems(
    @Embedded
    val note: Note,
    @Relation(parentColumn = "id", entityColumn = "note_id")
    val listItems: List<ListItem> = mutableListOf()
)
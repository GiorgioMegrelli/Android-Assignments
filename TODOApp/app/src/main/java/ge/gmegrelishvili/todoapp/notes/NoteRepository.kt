package ge.gmegrelishvili.todoapp.notes

import ge.gmegrelishvili.todoapp.database.dao.ListItemDao
import ge.gmegrelishvili.todoapp.database.dao.NoteDao
import ge.gmegrelishvili.todoapp.notes.model.NoteModel

class NoteRepository(val noteDao: NoteDao, val listItemDao: ListItemDao) {
    fun addNote(noteModel: NoteModel) {}

    fun getNotes(): List<NoteModel> {
        return listOf()
    }

    fun getNote(id: Int): NoteModel {
        return NoteModel(0, "NoteModel", true)
    }

    fun update(noteModel: NoteModel) {}
}
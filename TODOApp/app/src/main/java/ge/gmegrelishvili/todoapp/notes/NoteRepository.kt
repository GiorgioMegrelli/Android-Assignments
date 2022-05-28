package ge.gmegrelishvili.todoapp.notes

import ge.gmegrelishvili.todoapp.database.dao.ListItemDao
import ge.gmegrelishvili.todoapp.database.dao.NoteDao
import ge.gmegrelishvili.todoapp.database.entity.ListItem
import ge.gmegrelishvili.todoapp.database.entity.Note
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems

class NoteRepository(
    private val noteDao: NoteDao,
    private val listItemDao: ListItemDao
) {

    private val validChars = ('a'..'z') + ('0'..'9')

    private fun createRandomString(length: Int): String {
        return (1..length).map {
            validChars.random()
        }.joinToString("")
    }

    private fun createRandomTitle(): String {
        return "Title-${createRandomString(8)}"
    }


    fun getWLINotes(): List<NoteWithListItems> {
        return noteDao.getWLINotes()
    }

    fun getWLINotes(searchText: String): List<NoteWithListItems> {
        return noteDao.getWLINotes()
    }

    fun getWLINote(id: Int): NoteWithListItems {
        return noteDao.getWLINote(id)
    }

    fun insertNote(
        title: String?,
        isPinned: Boolean,
        listItems: List<Pair<String, Boolean>>
    ) {
        var titleVar = if (title == null || title.trim().isEmpty()) {
            createRandomTitle()
        } else {
            title
        }
        val insertedId = noteDao.insert(Note(title = titleVar, isPinned = isPinned)).toInt()
        for (pair in listItems) {
            listItemDao.insertListItem(
                ListItem(
                    noteId = insertedId,
                    value = pair.first,
                    checked = pair.second
                )
            )
        }
    }

    fun updateNote(
        id: Int,
        title: String?,
        isPinned: Boolean,
        listItems: List<Pair<String, Boolean>> = listOf(),
        newListItems: List<Pair<String, Boolean>> = listOf(),
        deleteListItems: List<Pair<String, Boolean>> = listOf(),
    ) {
        var titleVar: String? = title
        if (title != null && title.trim().isEmpty()) {
            titleVar = null
        }
        noteDao.update(id, titleVar, isPinned, System.currentTimeMillis())
    }
}
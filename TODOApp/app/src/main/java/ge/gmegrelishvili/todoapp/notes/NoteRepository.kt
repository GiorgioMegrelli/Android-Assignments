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

    private val randomTitleLength = 5
    private val randomTodoItemValueLength = 8

    private fun createRandomString(length: Int): String {
        return (1..length).map {
            validChars.random()
        }.joinToString("")
    }

    private fun createRandomTitle(): String {
        return "Title-${createRandomString(randomTitleLength)}"
    }

    private fun createRandomTodoItemValue(): String {
        return "Item-${createRandomString(randomTodoItemValueLength)}"
    }

    private fun prepareString(str: String?, ifEmpty: String): String {
        return if (str == null || str.trim().isEmpty()) {
            ifEmpty
        } else {
            str
        }
    }


    fun getWLINotes(searchText: String?): List<NoteWithListItems> {
        if (searchText == null || searchText.trim().isEmpty()) {
            return noteDao.getWLINotes()
        }
        return noteDao.getWLINotes(searchText)
    }

    fun getWLINote(id: Int): NoteWithListItems {
        return noteDao.getWLINote(id)
    }

    fun insertNote(
        title: String?,
        isPinned: Boolean,
        listItems: List<Pair<Boolean, String?>>
    ) {
        val titleVar = prepareString(title, createRandomTitle())
        val insertedId = noteDao.insert(Note(title = titleVar, isPinned = isPinned)).toInt()
        for (pair in listItems) {
            val value = prepareString(pair.second, createRandomTodoItemValue())
            val item = ListItem(noteId = insertedId, checked = pair.first, value = value)
            listItemDao.insertListItem(item)
        }
    }

    fun updateNote(
        id: Int,
        title: String?,
        isPinned: Boolean,
        listItems: List<ListItem>,
        newListItems: List<Pair<String?, Boolean>>,
        deleteListItemIds: Set<Int>,
    ) {
        var titleVar: String? = title
        if (title != null && title.trim().isEmpty()) {
            titleVar = null
        }
        noteDao.update(id, titleVar, isPinned, System.currentTimeMillis())
        for (itemPair in newListItems) {
            val value = prepareString(itemPair.first, createRandomTodoItemValue())
            val newItem = ListItem(noteId = id, checked = itemPair.second, value = value)
            listItemDao.insertListItem(newItem)
        }
        for (item in listItems) {
            if (!deleteListItemIds.contains(item.id)) {
                listItemDao.updateListItem(item.id, item.checked, item.value)
            }
        }
        for (itemId in deleteListItemIds) {
            listItemDao.deleteListItem(itemId)
        }
    }
}
package ge.gmegrelishvili.todoapp.notes.notehelper

import ge.gmegrelishvili.todoapp.notes.NoteActivity

class NumberUtils {
    companion object {
        fun parseInt(str: String?): Int? {
            if (str == NoteActivity.NoId) {
                return null
            }
            return try {
                str?.toInt()
            } catch (e: Exception) {
                null
            }
        }
    }
}
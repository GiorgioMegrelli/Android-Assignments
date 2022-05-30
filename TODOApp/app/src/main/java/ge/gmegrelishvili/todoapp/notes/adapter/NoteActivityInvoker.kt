package ge.gmegrelishvili.todoapp.notes.adapter

interface NoteActivityInvoker {
    fun checked(localId: Int, isChecked: Boolean)

    fun remove(localId: Int)
}
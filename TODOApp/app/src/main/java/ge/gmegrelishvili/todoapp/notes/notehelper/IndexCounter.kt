package ge.gmegrelishvili.todoapp.notes.notehelper

class IndexCounter(private var initValue: Int = 0) {

    fun nextIndex(): Int = initValue++
}
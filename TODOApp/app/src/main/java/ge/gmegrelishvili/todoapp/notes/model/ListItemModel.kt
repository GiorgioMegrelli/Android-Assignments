package ge.gmegrelishvili.todoapp.notes.model

data class ListItemModel(
    val id: Int,
    val noteId: Int,
    val checked: Boolean,
    val value: String?,
)

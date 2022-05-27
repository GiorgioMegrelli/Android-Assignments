package ge.gmegrelishvili.todoapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val isPinned: Boolean = false,
    @ColumnInfo(name = "edit_date") val editDate: Long = System.currentTimeMillis()
)

package ge.gmegrelishvili.todoapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var title: String,
    @ColumnInfo(name = "is_pinned") var isPinned: Boolean = false,
    @ColumnInfo(name = "edit_date") var editDate: Long = System.currentTimeMillis()
)

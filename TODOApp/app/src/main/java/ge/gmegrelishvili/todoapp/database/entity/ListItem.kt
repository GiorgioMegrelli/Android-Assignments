package ge.gmegrelishvili.todoapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = ["id"],
        childColumns = ["note_id"]
    )]
)
data class ListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_id") val noteId: Int,
    @ColumnInfo val checked: Boolean = false,
    @ColumnInfo val value: String?,
)

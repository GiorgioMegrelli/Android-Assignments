package ge.gmegrelishvili.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import ge.gmegrelishvili.todoapp.database.entity.Note
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems

@Dao
interface NoteDao {
    @Query("select * from Note order by edit_date desc")
    // Notes With ListItems
    fun getWLINotes(): List<NoteWithListItems>

    @Query("select * from Note where id=:id")
    fun getWLINote(id: Int): NoteWithListItems

    @Update
    fun update(note: Note)
}
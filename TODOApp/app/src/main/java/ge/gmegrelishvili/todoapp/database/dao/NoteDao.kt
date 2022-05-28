package ge.gmegrelishvili.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ge.gmegrelishvili.todoapp.database.entity.Note
import ge.gmegrelishvili.todoapp.database.entity.NoteWithListItems

@Dao
abstract class NoteDao {
    @Query("select * from Note order by edit_date desc")
    abstract fun getWLINotes(): List<NoteWithListItems>  // Notes With ListItems

    @Query(
        """
        select * from Note
        where title like '%' || :searchText || '%'
        order by edit_date desc
    """
    )
    abstract fun getWLINotes(searchText: String): List<NoteWithListItems>

    @Query("select * from Note where id=:id")
    abstract fun getWLINote(id: Int): NoteWithListItems

    @Insert
    abstract fun insert(note: Note): Long

    @Query(
        """
        UPDATE Note
        SET title = COALESCE(:titleNW, title),
            is_pinned = :isPinnedNW,
            edit_date = :newEditDate
        WHERE id = :id
    """
    )
    abstract fun update(id: Int, titleNW: String?, isPinnedNW: Boolean, newEditDate: Long)
}
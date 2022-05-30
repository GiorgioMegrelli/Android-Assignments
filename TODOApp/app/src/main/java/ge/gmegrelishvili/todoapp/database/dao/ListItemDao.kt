package ge.gmegrelishvili.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ge.gmegrelishvili.todoapp.database.entity.ListItem

@Dao
abstract class ListItemDao {
    @Query("select * from ListItem")
    abstract fun getListItems(): List<ListItem>

    @Insert
    abstract fun insertListItem(listItem: ListItem)

    @Query(
        """
        update ListItem
        set checked = :checkedNW,
            value = COALESCE(:valueNW, value)
        where id = :id
    """
    )
    abstract fun updateListItem(id: Int, checkedNW: Boolean, valueNW: String)

    @Query("delete from ListItem where id = :id")
    abstract fun deleteListItem(id: Int)
}
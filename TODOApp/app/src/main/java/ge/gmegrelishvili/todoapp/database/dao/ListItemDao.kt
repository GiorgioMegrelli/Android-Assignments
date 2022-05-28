package ge.gmegrelishvili.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import ge.gmegrelishvili.todoapp.database.entity.ListItem

@Dao
abstract class ListItemDao {
    @Insert
    abstract fun insertListItem(listItem: ListItem)
}
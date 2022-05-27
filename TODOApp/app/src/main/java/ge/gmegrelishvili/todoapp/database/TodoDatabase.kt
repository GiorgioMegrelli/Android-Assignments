package ge.gmegrelishvili.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ge.gmegrelishvili.todoapp.database.dao.ListItemDao
import ge.gmegrelishvili.todoapp.database.dao.NoteDao
import ge.gmegrelishvili.todoapp.database.entity.ListItem
import ge.gmegrelishvili.todoapp.database.entity.Note

@Database(version = 1, entities = [Note::class, ListItem::class])
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    abstract fun getListItemDao(): ListItemDao

    companion object {
        private const val databaseName = "todoapp-database"

        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room
                            .databaseBuilder(context, TodoDatabase::class.java, databaseName)
                            .build()
                    }
                }
            }
            return instance!!
        }
    }
}
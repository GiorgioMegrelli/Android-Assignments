package ge.gmegrelishvili.alarmapp.resources

import android.content.Context

class StringReader(private val context: Context) {

    fun getString(stringResId: Int): String {
        return context.resources.getString(stringResId)
    }

}
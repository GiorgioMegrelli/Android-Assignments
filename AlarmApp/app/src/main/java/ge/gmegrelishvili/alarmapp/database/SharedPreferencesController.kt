package ge.gmegrelishvili.alarmapp.database

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import ge.gmegrelishvili.alarmapp.R

class SharedPreferencesController(
    private val activity: Activity,
    private val allowDuplicate: Boolean = false
) {

    private val resources = activity.resources

    fun getSharedPreferences(stringResourceId: Int): SharedPreferences {
        return createSharedPreferences(getKey(stringResourceId))
    }

    fun getSharedPreferences(text: String): SharedPreferences {
        return createSharedPreferences(getKey(text))
    }

    private fun createSharedPreferences(key: String): SharedPreferences {
        if (allowDuplicate) {
            return activity.getSharedPreferences(key, Context.MODE_PRIVATE)
        }
        var instance = instances[key]
        if (instance == null) {
            instance = activity.getSharedPreferences(key, Context.MODE_PRIVATE)
            instances[key] = instance
        }
        return instance!!
    }

    private fun getKey(stringResourceId: Int): String {
        return getKey(resources.getString(stringResourceId))
    }

    private fun getKey(text: String): String {
        return resources.getString(R.string.sp_global_fmt).format(text)
    }

    companion object {
        private val instances = HashMap<String, SharedPreferences>()
    }
}
package ge.gmegrelishvili.alarmapp.database.repository

import android.app.Activity
import ge.gmegrelishvili.alarmapp.database.SharedPreferencesController

class AppConfigRepository(activity: Activity) {

    private val sharedPreferences =
        SharedPreferencesController(activity).getSharedPreferences(APP_CONFIG_SP_KEY)

    fun getValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun insertValue(key: String, value: String): Boolean {
        val hasKey = sharedPreferences.contains(key)
        Thread {
            with(sharedPreferences.edit()) {
                putString(key, value)
                apply()
            }
        }.start()
        return !hasKey
    }

    companion object {
        const val APP_CONFIG_SP_KEY = "SP_APP_CONFIGS"
    }
}
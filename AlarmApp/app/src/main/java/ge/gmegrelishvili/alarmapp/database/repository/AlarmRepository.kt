package ge.gmegrelishvili.alarmapp.database.repository

import android.app.Activity
import ge.gmegrelishvili.alarmapp.alarm.AlarmTime24H
import ge.gmegrelishvili.alarmapp.database.SharedPreferencesController

class AlarmRepository(activity: Activity) {

    private val sharedPreferences =
        SharedPreferencesController(activity).getSharedPreferences(ALARM_SP_KEY)

    fun getAlarms(): List<AlarmTime24H> {
        val result = mutableListOf<AlarmTime24H>()
        sharedPreferences.all.forEach {
            val text = it.key
            val scheduled = it.value as Boolean
            result.add(AlarmTime24H.parseTotalMinutesText(text, scheduled))
        }
        result.sortBy {
            it.toTotalMinutes()
        }
        return result
    }

    fun insertAlarm(alarm: AlarmTime24H): Boolean {
        val key = alarm.toTotalMinutes().toString()
        val hasKey = sharedPreferences.contains(key)
        Thread {
            with(sharedPreferences.edit()) {
                putBoolean(key, alarm.scheduled)
                apply()
            }
        }.start()
        return !hasKey
    }

    fun removeAlarm(alarm: AlarmTime24H): Boolean {
        val key = alarm.toTotalMinutes().toString()
        val hasKey = sharedPreferences.contains(key)
        if (hasKey) {
            Thread {
                with(sharedPreferences.edit()) {
                    remove(key)
                    apply()
                }
            }.start()
        }
        return hasKey
    }

    companion object {
        const val ALARM_SP_KEY = "SP_ALARM_TIMES"
    }
}
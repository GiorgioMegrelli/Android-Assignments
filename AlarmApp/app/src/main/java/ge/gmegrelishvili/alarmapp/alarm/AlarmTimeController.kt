package ge.gmegrelishvili.alarmapp.alarm

import java.util.*

class AlarmTimeController {
    companion object {
        const val MILLIS_IN_SECOND = 1000L
        const val SECONDS_IN_MINUTE = 60
        const val MINUTES_IN_HOUR = 60
        const val HOURS_IN_DAY = 24
        const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * SECONDS_IN_MINUTE
        const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * MINUTES_IN_HOUR

        fun millisToAlarm(totalMinutes: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, totalMinutes / MINUTES_IN_HOUR)
            calendar.set(Calendar.MINUTE, totalMinutes % MINUTES_IN_HOUR)
            calendar.set(Calendar.SECOND, 0)
            var timeDiff = calendar.timeInMillis - System.currentTimeMillis()
            if (timeDiff <= 0) {
                timeDiff += MILLIS_IN_HOUR * HOURS_IN_DAY
            }
            return timeDiff
        }
    }
}
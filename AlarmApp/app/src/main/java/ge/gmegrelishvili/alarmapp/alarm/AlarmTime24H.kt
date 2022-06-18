package ge.gmegrelishvili.alarmapp.alarm

class AlarmTime24H(val hour: Int, val minute: Int, var scheduled: Boolean) {
    fun toHrMm(): String {
        return toHrMmString(hour, minute)
    }

    fun toTotalMinutes(): Int {
        return hour * AlarmTimeController.MINUTES_IN_HOUR + minute
    }

    companion object {
        fun parseTotalMinutesText(totalMinutesText: String, scheduled: Boolean): AlarmTime24H {
            val totalMinutes = totalMinutesText.trim().toInt()
            return AlarmTime24H(
                totalMinutes / AlarmTimeController.MINUTES_IN_HOUR,
                totalMinutes % AlarmTimeController.MINUTES_IN_HOUR,
                scheduled
            )
        }

        fun toHrMmString(hour: Int, minute: Int): String {
            val hhStr = hour.toString().padStart(2, '0')
            val mmStr = minute.toString().padStart(2, '0')
            return "$hhStr:$mmStr"
        }
    }

}
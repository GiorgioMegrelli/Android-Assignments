package ge.gmegrelishvili.alarmapp.alarm

class AlarmTime24H(val hour: Int, val minute: Int, var scheduled: Boolean) {
    fun toHrMm(): String {
        val hhStr = hour.toString().padStart(2, '0')
        val mmStr = minute.toString().padStart(2, '0')
        return "$hhStr:$mmStr"
    }

    fun toTotalMinutes(): Int {
        return hour * MINUTES_IN_HOUR + minute
    }

    companion object {
        const val MINUTES_IN_HOUR = 60

        fun parseTotalMinutesText(totalMinutesText: String, scheduled: Boolean): AlarmTime24H {
            val totalMinutes = totalMinutesText.trim().toInt()
            return AlarmTime24H(
                totalMinutes / MINUTES_IN_HOUR,
                totalMinutes % MINUTES_IN_HOUR,
                scheduled
            )
        }
    }
}
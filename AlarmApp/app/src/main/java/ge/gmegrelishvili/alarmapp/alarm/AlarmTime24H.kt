package ge.gmegrelishvili.alarmapp.alarm

class AlarmTime24H(val hour: Int, val minute: Int) {
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
    }
}
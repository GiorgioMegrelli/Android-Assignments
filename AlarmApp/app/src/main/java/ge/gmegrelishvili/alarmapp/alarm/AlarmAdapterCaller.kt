package ge.gmegrelishvili.alarmapp.alarm

interface AlarmAdapterCaller {

    fun updateAlarmInvoked(totalMinutes: Int, value: Boolean)

    fun removeAlarmInvoked(totalMinutes: Int)
}
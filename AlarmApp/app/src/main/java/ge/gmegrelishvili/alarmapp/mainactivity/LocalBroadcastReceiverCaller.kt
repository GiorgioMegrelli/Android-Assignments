package ge.gmegrelishvili.alarmapp.mainactivity

interface LocalBroadcastReceiverCaller {

    fun cancelAlarm(totalMinutes: Int)

    fun snoozeAlarm(totalMinutes: Int)
}
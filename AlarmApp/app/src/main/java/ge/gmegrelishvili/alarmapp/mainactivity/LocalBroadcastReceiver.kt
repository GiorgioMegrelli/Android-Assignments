package ge.gmegrelishvili.alarmapp.mainactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ge.gmegrelishvili.alarmapp.MainActivity

class LocalBroadcastReceiver(private val caller: LocalBroadcastReceiverCaller) :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.let {
                val totalMinutes = intent.getIntExtra(
                    MainActivity.INTENT_ARG_BACK_TO_ACTIVITY,
                    MainActivity.NO_PARAM_ARG
                )
                val actionType = intent.getIntExtra(
                    MainActivity.INTENT_ARG_BACK_TO_ACTIVITY_2,
                    MainActivity.NO_PARAM_ARG
                )
                if (totalMinutes != MainActivity.NO_PARAM_ARG && actionType != MainActivity.NO_PARAM_ARG) {
                    when (actionType) {
                        MainActivity.ALARM_CANCELED -> caller.cancelAlarm(totalMinutes)
                        MainActivity.ALARM_SNOOZED -> caller.snoozeAlarm(totalMinutes)
                    }
                }
            }
        }
    }
}
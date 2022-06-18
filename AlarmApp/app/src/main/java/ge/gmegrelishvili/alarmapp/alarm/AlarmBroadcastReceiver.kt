package ge.gmegrelishvili.alarmapp.alarm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ge.gmegrelishvili.alarmapp.MainActivity
import ge.gmegrelishvili.alarmapp.R
import ge.gmegrelishvili.alarmapp.resources.StringReader

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.let {
                when (intent.action.toString()) {
                    ALARM_ACTION -> alarmFinished(context, intent)
                    NOTIFICATION_CANCEL_CLICK -> alarmCanceled(context, intent)
                    NOTIFICATION_SNOOZE_CLICK -> alarmSnoozed(context, intent)
                }
            }
        }
    }

    private fun alarmFinished(context: Context, intent: Intent) {
        val totalMinutes = intent.getIntExtra(INTENT_ARG_TOTAL_MINUTES, -1)
        val notificationManager = NotificationManagerCompat.from(context)
        val notification = buildNotification(context, totalMinutes)
        notificationManager.notify(NOTIFICATION_TAG, totalMinutes, notification)
    }

    private fun alarmCanceled(context: Context, intent: Intent) {
        sendBackToActivity(context, intent, MainActivity.ALARM_CANCELED)
    }

    private fun alarmSnoozed(context: Context, intent: Intent) {
        sendBackToActivity(context, intent, MainActivity.ALARM_SNOOZED)
    }

    private fun sendBackToActivity(context: Context, intent: Intent, actionType: Int) {
        val totalMinutes =
            intent.getIntExtra(INTENT_ARG_TOTAL_MINUTES, MainActivity.NO_TOTAL_MINUTES)

        NotificationManagerCompat.from(context).cancel(NOTIFICATION_TAG, totalMinutes)

        context.sendBroadcast(Intent(ALARM_BROADCAST_RECEIVER_ACTIVITY).apply {
            putExtra(MainActivity.INTENT_ARG_BACK_TO_ACTIVITY, totalMinutes)
            putExtra(MainActivity.INTENT_ARG_BACK_TO_ACTIVITY_2, actionType)
        })
    }

    private fun buildNotification(context: Context, totalMinutes: Int): Notification {
        val resStrings = StringReader(context)
        val title = resStrings.getString(R.string.notification_title)
        val text = resStrings.getString(R.string.notification_text).format(
            AlarmTime24H.toHrMmString(
                totalMinutes / AlarmTimeController.MINUTES_IN_HOUR,
                totalMinutes % AlarmTimeController.MINUTES_IN_HOUR
            )
        )
        val cancelStr = resStrings.getString(R.string.notification_cancel_btn_txt)
        val snoozeStr = resStrings.getString(R.string.notification_snooze_btn_txt)

        val contentIntent = PendingIntent.getActivity(
            context,
            ALARM_REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            FLAG_IMMUTABLE
        )
        val cancelIntent =
            createActionedPIntent(context, NOTIFICATION_CANCEL_CLICK, totalMinutes)
        val snoozeIntent =
            createActionedPIntent(context, NOTIFICATION_SNOOZE_CLICK, totalMinutes)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm_clock)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(contentIntent)
            .addAction(NO_ICON, cancelStr, cancelIntent)
            .addAction(NO_ICON, snoozeStr, snoozeIntent)
            .build()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createActionedPIntent(
        context: Context,
        actionName: String,
        totalMinutes: Int
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context, ALARM_REQUEST_CODE,
            Intent(actionName).apply {
                `package` = context.packageName
                putExtra(INTENT_ARG_TOTAL_MINUTES, totalMinutes)
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    class CreateAlarmIntentBuilder(private val mainActivity: MainActivity) {
        fun build(totalMinutes: Int): PendingIntent {
            val intent = Intent(mainActivity, AlarmBroadcastReceiver::class.java)
            intent.action = ALARM_ACTION
            intent.putExtra(INTENT_ARG_TOTAL_MINUTES, totalMinutes)
            return PendingIntent.getBroadcast(
                mainActivity, ALARM_REQUEST_CODE, intent, FLAG_IMMUTABLE
            )
        }
    }

    companion object {
        const val NO_ICON = 0
        const val ALARM_REQUEST_CODE = 200
        const val ALARM_BROADCAST_RECEIVER_ACTIVITY = "ALARM_BROADCAST_RECEIVER_ACTIVITY"
        const val CHANNEL_ID = "ge.gmegrelishvili.alarmapp.ALARM"
        const val NOTIFICATION_TAG = "ALARM_FINISHED"

        const val ALARM_ACTION = "ge.gmegrelishvili.alarmapp.ALARM_ACTION"
        const val NOTIFICATION_CANCEL_CLICK = "ge.gmegrelishvili.alarmapp.NOTIFICATION_CANCEL_CLICK"
        const val NOTIFICATION_SNOOZE_CLICK = "ge.gmegrelishvili.alarmapp.NOTIFICATION_SNOOZE_CLICK"

        private const val INTENT_ARG_TOTAL_MINUTES = "INTENT_ARG_TOTAL_MINUTES"
    }

}
package ge.gmegrelishvili.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ge.gmegrelishvili.alarmapp.alarm.*
import ge.gmegrelishvili.alarmapp.mainactivity.LocalBroadcastReceiver
import ge.gmegrelishvili.alarmapp.mainactivity.LocalBroadcastReceiverCaller
import ge.gmegrelishvili.alarmapp.resources.StringReader
import java.util.*

class MainActivity : AppCompatActivity(), AlarmAdapterCaller, LocalBroadcastReceiverCaller {

    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter

    private lateinit var alarmManager: AlarmManager
    private lateinit var intendBuilder: AlarmBroadcastReceiver.CreateAlarmIntentBuilder
    private lateinit var broadcastReceiver: BroadcastReceiver

    private lateinit var switchButton: TextView
    private lateinit var alarmButton: ImageView

    private val storedAlarms = HashMap<Int, AlarmTime24H>()
    private val startedAlarms = HashMap<Int, PendingIntent>()

    private val viewModel: AlarmViewModel by lazy {
        ViewModelProvider(
            this, AlarmViewModel.Companion.NoteViewModelFactory(this)
        ).get(AlarmViewModel::class.java)
    }
    private var appUiMode: Int? = null

    private val resStrings = StringReader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchButton = findViewById(R.id.switch_mode_button)
        alarmButton = findViewById(R.id.add_alarm_button)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intendBuilder = AlarmBroadcastReceiver.CreateAlarmIntentBuilder(this)

        appUiMode = getUiMode()
        setAppUiMode()

        loadData()
        setUpAdapters()
        drawAlarms()

        switchButton.setOnClickListener {
            switchUiMode()
        }
        alarmButton.setOnClickListener {
            createAlarm()
        }

        broadcastReceiver = LocalBroadcastReceiver(this)
        registerReceiver(
            broadcastReceiver,
            IntentFilter(AlarmBroadcastReceiver.ALARM_BROADCAST_RECEIVER_ACTIVITY)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun switchUiMode() {
        appUiMode = if (appUiMode != null && appUiMode == APP_MODE_LIGHT) {
            APP_MODE_DARK
        } else {
            APP_MODE_LIGHT
        }
        viewModel.insertConfigValue(appUiMode!!)
        setAppUiMode()
    }

    private fun setAppUiMode() {
        val buttonFmt = resStrings.getString(R.string.switch_mode_button_text_fmt)
        appUiMode?.let {
            AppCompatDelegate.setDefaultNightMode(it)
            switchButton.text = buttonFmt.format(
                if (it == APP_MODE_LIGHT) {
                    resStrings.getString(R.string.switch_mode_button_text_dark)
                } else {
                    resStrings.getString(R.string.switch_mode_button_text_light)
                }
            )
        }
    }

    private fun getUiMode(): Int {
        val storedMode = viewModel.getConfigValue()
        if (storedMode != null) {
            return storedMode
        }
        val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return if (mode == Configuration.UI_MODE_NIGHT_YES) {
            APP_MODE_DARK
        } else {
            APP_MODE_LIGHT
        }
    }

    private fun loadData() {
        val alarms = viewModel.getAlarms()
        for (alarm in alarms) {
            storedAlarms[alarm.toTotalMinutes()] = alarm
        }
    }

    private fun setUpAdapters() {
        alarmRecyclerView = findViewById(R.id.alarms)
        alarmAdapter = AlarmAdapter(this)

        alarmRecyclerView.addItemDecoration(AlarmAdapterItemDecoration(this))
        alarmRecyclerView.adapter = alarmAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun drawAlarms() {
        val alarms = ArrayList(storedAlarms.values)
        alarms.sortBy {
            it.toTotalMinutes()
        }

        alarmAdapter.clear()
        for (alarm in alarms) {
            alarmAdapter.add(alarm)
        }
        alarmRecyclerView.post {
            alarmAdapter.notifyDataSetChanged()
        }
    }

    private fun createAlarm() {
        val timePicker = createTimePicker()

        timePicker.show(supportFragmentManager, TIME_PICKER_TAG)

        timePicker.addOnPositiveButtonClickListener {
            val alarm = AlarmTime24H(timePicker.hour, timePicker.minute, true)
            viewModel.insertAlarm(alarm)
            storedAlarms[alarm.toTotalMinutes()] = alarm
            drawAlarms()
            setUpAlarm(alarm.toTotalMinutes(), true)
        }
    }

    private fun createTimePicker(): MaterialTimePicker {
        val currTime = Calendar.getInstance()
        val timeFormat =
            if (DateFormat.is24HourFormat(this)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        currTime.add(Calendar.MINUTE, 1)
        return MaterialTimePicker.Builder()
            .setTimeFormat(timeFormat)
            .setHour(currTime.get(Calendar.HOUR_OF_DAY))
            .setMinute(currTime.get(Calendar.MINUTE))
            .setTitleText(TIME_PICKER_TITLE_TEXT)
            .build()
    }

    override fun updateAlarmInvoked(totalMinutes: Int, value: Boolean) {
        setUpAlarm(totalMinutes, value)
        updateAlarm(totalMinutes, value)
    }

    private fun setUpAlarm(totalMinutes: Int, alarmStarted: Boolean, customMillis: Long? = null) {
        val hasKey = startedAlarms.containsKey(totalMinutes)
        if (alarmStarted && !hasKey) {
            val intent = intendBuilder.build(totalMinutes)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                customMillis ?: AlarmTimeController.millisToAlarm(totalMinutes),
                intent
            )
            startedAlarms[totalMinutes] = intent
        } else if (!alarmStarted && hasKey) {
            alarmManager.cancel(startedAlarms[totalMinutes])
            startedAlarms.remove(totalMinutes)
        }
    }

    private fun updateAlarm(totalMinutes: Int, value: Boolean) {
        if (storedAlarms[totalMinutes] != null) {
            storedAlarms[totalMinutes]!!.scheduled = value
            viewModel.insertAlarm(storedAlarms[totalMinutes]!!)
            drawAlarms()
        }
    }

    override fun removeAlarmInvoked(totalMinutes: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE && storedAlarms[totalMinutes] != null) {
                viewModel.removeAlarm(storedAlarms[totalMinutes]!!)
                storedAlarms.remove(totalMinutes)
                setUpAlarm(totalMinutes, false)
                drawAlarms()
            }
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(resStrings.getString(R.string.dialog_question_str))
            .setNegativeButton(resStrings.getString(R.string.dialog_no_str), listener)
            .setPositiveButton(resStrings.getString(R.string.dialog_yes_str), listener)
            .show()
    }

    override fun cancelAlarm(totalMinutes: Int) {
        setUpAlarm(totalMinutes, false)
        updateAlarm(totalMinutes, false)
        drawAlarms()
    }

    override fun snoozeAlarm(totalMinutes: Int) {
        setUpAlarm(totalMinutes, false)
        setUpAlarm(totalMinutes, true, AlarmTimeController.MILLIS_IN_MINUTE)
        drawAlarms()
    }

    companion object {
        const val NO_TOTAL_MINUTES = -1
        const val NO_PARAM_ARG = -2
        const val ALARM_CANCELED = 1
        const val ALARM_SNOOZED = 2
        const val INTENT_ARG_BACK_TO_ACTIVITY = "INTENT_ARG_BACK_TO_ACTIVITY"
        const val INTENT_ARG_BACK_TO_ACTIVITY_2 = "INTENT_ARG_BACK_TO_ACTIVITY_2"
        const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
        const val TIME_PICKER_TITLE_TEXT = "Select Alarm Time"

        private const val APP_MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES
        private const val APP_MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    }

}
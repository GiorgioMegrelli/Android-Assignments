package ge.gmegrelishvili.alarmapp

import android.annotation.SuppressLint
import android.content.DialogInterface
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

class MainActivity : AppCompatActivity(), AlarmAdapterCaller {

    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter

    private lateinit var switchButton: TextView
    private lateinit var alarmButton: ImageView

    private val alarmMap = HashMap<Int, AlarmTime24H>()
    private val viewModel: AlarmViewModel by lazy {
        ViewModelProvider(
            this, AlarmViewModel.Companion.NoteViewModelFactory(this)
        ).get(AlarmViewModel::class.java)
    }
    private var appUiMode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchButton = findViewById(R.id.switch_mode_button)
        alarmButton = findViewById(R.id.add_alarm_button)

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
        val buttonFmt = getResString(R.string.switch_mode_button_text_fmt)
        appUiMode?.let {
            AppCompatDelegate.setDefaultNightMode(it)
            switchButton.text = buttonFmt.format(
                if (it == APP_MODE_LIGHT) {
                    getResString(R.string.switch_mode_button_text_dark)
                } else {
                    getResString(R.string.switch_mode_button_text_light)
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
            alarmMap[alarm.toTotalMinutes()] = alarm
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
        val alarms = ArrayList(alarmMap.values)
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
            alarmMap[alarm.toTotalMinutes()] = alarm
            drawAlarms()
        }
    }

    private fun createTimePicker(): MaterialTimePicker {
        val timeFormat =
            if (DateFormat.is24HourFormat(this)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        return MaterialTimePicker.Builder()
            .setTimeFormat(timeFormat)
            .setHour(TIME_PICKER_DEFAULT_HOUR)
            .setTitleText(TIME_PICKER_TITLE_TEXT)
            .build()
    }

    override fun updateAlarmInvoked(totalMinutes: Int, value: Boolean) {
        if (alarmMap[totalMinutes] != null) {
            alarmMap[totalMinutes]!!.scheduled = value
            viewModel.insertAlarm(alarmMap[totalMinutes]!!)
            drawAlarms()
        }
    }

    override fun removeAlarmInvoked(totalMinutes: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE && alarmMap[totalMinutes] != null) {
                viewModel.removeAlarm(alarmMap[totalMinutes]!!)
                alarmMap.remove(totalMinutes)
                drawAlarms()
            }
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(getResString(R.string.dialog_question_str))
            .setNegativeButton(getResString(R.string.dialog_no_str), listener)
            .setPositiveButton(getResString(R.string.dialog_yes_str), listener)
            .show()
    }

    private fun getResString(stringResId: Int): String {
        return resources.getString(stringResId)
    }

    companion object {
        const val TIME_PICKER_DEFAULT_HOUR = 12
        const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
        const val TIME_PICKER_TITLE_TEXT = "Select Alarm Time"

        private const val APP_MODE_DARK = AppCompatDelegate.MODE_NIGHT_YES
        private const val APP_MODE_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    }
}
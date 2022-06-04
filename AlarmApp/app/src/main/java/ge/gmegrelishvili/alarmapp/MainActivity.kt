package ge.gmegrelishvili.alarmapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ge.gmegrelishvili.alarmapp.alarm.AlarmAdapter
import ge.gmegrelishvili.alarmapp.alarm.AlarmAdapterItemDecoration
import ge.gmegrelishvili.alarmapp.alarm.AlarmTime24H

class MainActivity : AppCompatActivity() {
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpAdapters()

        findViewById<TextView>(R.id.switch_mode_button).setOnClickListener {}

        findViewById<ImageView>(R.id.add_alarm_button).setOnClickListener {
            createAlarm()
        }
    }

    private fun setUpAdapters() {
        alarmRecyclerView = findViewById(R.id.alarms)
        alarmAdapter = AlarmAdapter()

        alarmRecyclerView.addItemDecoration(AlarmAdapterItemDecoration(this))
        alarmRecyclerView.adapter = alarmAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createAlarm() {
        val timePicker = createTimePicker()

        timePicker.show(supportFragmentManager, TIME_PICKER_TAG)

        timePicker.addOnPositiveButtonClickListener {
            val alarm = AlarmTime24H(timePicker.hour, timePicker.minute)
            alarmAdapter.add(alarm)
            alarmRecyclerView.post {
                alarmAdapter.notifyDataSetChanged()
            }
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

    companion object {
        const val TIME_PICKER_DEFAULT_HOUR = 12
        const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
        const val TIME_PICKER_TITLE_TEXT = "Select Alarm Time"
    }
}
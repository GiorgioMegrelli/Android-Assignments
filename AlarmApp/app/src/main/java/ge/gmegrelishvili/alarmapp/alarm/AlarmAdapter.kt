package ge.gmegrelishvili.alarmapp.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import ge.gmegrelishvili.alarmapp.R

class AlarmAdapter(private val adapterCaller: AlarmAdapterCaller) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private val alarms = mutableListOf<AlarmTime24H>()

    fun add(alarm: AlarmTime24H) {
        alarms.add(alarm)
    }

    fun clear() {
        alarms.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.fragment_alarm, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarms[position])
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    inner class AlarmViewHolder(private val alarmView: View) : RecyclerView.ViewHolder(alarmView) {
        fun bind(alarm: AlarmTime24H) {
            val hhMmTextView = alarmView.findViewById<TextView>(R.id.hh_mm_text)
            val alarmSwitcher = alarmView.findViewById<SwitchMaterial>(R.id.alarm_switcher)

            hhMmTextView.text = alarm.toHrMm()
            alarmSwitcher.isChecked = alarm.scheduled

            alarmSwitcher.setOnCheckedChangeListener { _, isChecked ->
                adapterCaller.updateAlarmInvoked(alarm.toTotalMinutes(), isChecked)
            }

            alarmView.setOnLongClickListener {
                adapterCaller.removeAlarmInvoked(alarm.toTotalMinutes())
                true
            }
        }
    }
}
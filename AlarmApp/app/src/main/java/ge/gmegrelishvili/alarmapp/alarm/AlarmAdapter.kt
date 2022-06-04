package ge.gmegrelishvili.alarmapp.alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.alarmapp.R

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private val alarms = mutableListOf<AlarmTime24H>()

    fun add(alarm: AlarmTime24H) {
        alarms.add(alarm)
        alarms.sortBy {
            it.toTotalMinutes()
        }
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

    inner class AlarmViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(alarm: AlarmTime24H) {
            itemView.findViewById<TextView>(R.id.hh_mm_text).text = alarm.toHrMm()
        }
    }
}
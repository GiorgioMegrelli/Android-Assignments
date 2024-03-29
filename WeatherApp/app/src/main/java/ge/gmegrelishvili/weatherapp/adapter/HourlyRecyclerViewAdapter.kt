package ge.gmegrelishvili.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.model.HourlyInfoModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class HourlyRecyclerViewAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<HourlyRecyclerViewAdapter.HourlyViewHolder>() {

    private val items = mutableListOf<HourlyInfoModel>()

    inner class HourlyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(hourlyInfoModel: HourlyInfoModel) {
            val iconUrl =
                "https://openweathermap.org/img/wn/${hourlyInfoModel.weather[0].icon}@2x.png"
            val itemTime = SimpleDateFormat(
                "hh a dd MMM",
                Locale.US
            ).format(Date(hourlyInfoModel.date * 1000L))
            val temp = fragment.getString(R.string.today_text_temp_tmp).format(
                round(hourlyInfoModel.main.temperature).toInt()
            )
            val desc = hourlyInfoModel.weather[0].description

            Glide.with(fragment).load(iconUrl).into(itemView.findViewById(R.id.hourly_item_icon))

            itemView.findViewById<TextView>(R.id.hourly_item_time).text = itemTime
            itemView.findViewById<TextView>(R.id.hourly_item_temp).text = temp
            itemView.findViewById<TextView>(R.id.hourly_item_desc).text = desc
        }
    }

    fun updateAdapter(newItems: List<HourlyInfoModel>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
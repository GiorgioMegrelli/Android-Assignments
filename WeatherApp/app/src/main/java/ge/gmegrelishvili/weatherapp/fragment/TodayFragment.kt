package ge.gmegrelishvili.weatherapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import ge.gmegrelishvili.weatherapp.Country
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.model.DetailsModel
import androidx.fragment.app.setFragmentResultListener
import java.util.*
import kotlin.math.round

class TodayFragment : WeatherAppFragment() {

    companion object {
        const val RequestKey = "TodayFragmentRequestKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(HourlyFragment.RequestKey) { _, bundle ->
            bundle.getString(FlagIconClicked)?.let { show(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createdView = inflater.inflate(R.layout.fragment_today, container, false)
        show(Country.CapitalDefault)

        val iconGe = createdView.findViewById<ImageButton>(R.id.flag_icon_georgia)
        val iconUk = createdView.findViewById<ImageButton>(R.id.flag_icon_uk)
        val iconJa = createdView.findViewById<ImageButton>(R.id.flag_icon_jamaica)

        iconGe.setOnClickListener {
            show(Country.CapitalGe)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalGe))
        }
        iconUk.setOnClickListener {
            show(Country.CapitalUk)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalUk))
        }
        iconJa.setOnClickListener {
            show(Country.CapitalJa)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalJa))
        }
        return createdView
    }

    override fun show(cityName: String) {
        apiClient.getDetails(cityName) { t, response ->
            if (t != null) {
                showToast("Error Occurred with External Server$t")
            } else {
                val detailsModel = response as DetailsModel

                checkTime(Date(detailsModel.date * 1000L))

                val iconUrl =
                    "https://openweathermap.org/img/wn/${detailsModel.weather[0].icon}@2x.png"
                val temp = getString(R.string.today_text_temp_tmp).format(
                    round(detailsModel.main.temperature).toInt()
                )
                val desc = detailsModel.weather[0].description
                val pres = detailsModel.main.pressure.toString()
                val feelTemp = getString(R.string.today_text_temp_tmp).format(
                    round(detailsModel.main.feelsLike).toInt()
                )
                val humidity = getString(R.string.today_text_humidity_tmp).format(
                    detailsModel.main.humidity
                )

                Glide.with(this).load(iconUrl).into(createdView.findViewById(R.id.weather_icon))

                createdView.findViewById<TextView>(R.id.today_capital_name).text = cityName
                createdView.findViewById<TextView>(R.id.today_output_temp_big).text = temp
                createdView.findViewById<TextView>(R.id.weather_desc).text = desc
                createdView.findViewById<TextView>(R.id.today_output_temp).text = temp
                createdView.findViewById<TextView>(R.id.today_output_feel).text = feelTemp
                createdView.findViewById<TextView>(R.id.today_output_humidity).text = humidity
                createdView.findViewById<TextView>(R.id.today_output_pressure).text = pres
            }
        }
    }
}
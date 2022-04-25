package ge.gmegrelishvili.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import ge.gmegrelishvili.weatherapp.Country
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.model.DetailsModel
import java.util.*
import kotlin.math.round

class DetailsFragment() : WeatherAppFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createdView = inflater.inflate(R.layout.fragment_details, container, false)
        show(Country.CapitalDefault)
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
                val temp = getString(R.string.details_text_temp_tmp).format(
                    round(detailsModel.main.temperature).toInt()
                )
                val desc = detailsModel.weather[0].description
                val pres = detailsModel.main.pressure.toString()
                val feelTemp = getString(R.string.details_text_temp_tmp).format(
                    round(detailsModel.main.feelsLike).toInt()
                )
                val humidity = getString(R.string.details_text_humidity_tmp).format(
                    detailsModel.main.humidity
                )

                Glide.with(this).load(iconUrl).into(createdView.findViewById(R.id.weather_icon))

                createdView.findViewById<TextView>(R.id.details_capital_name).text = cityName
                createdView.findViewById<TextView>(R.id.details_output_temp_big).text = temp
                createdView.findViewById<TextView>(R.id.weather_desc).text = desc
                createdView.findViewById<TextView>(R.id.details_output_temp).text = temp
                createdView.findViewById<TextView>(R.id.details_output_feel).text = feelTemp
                createdView.findViewById<TextView>(R.id.details_output_humidity).text = humidity
                createdView.findViewById<TextView>(R.id.details_output_pressure).text = pres
            }
        }
    }
}
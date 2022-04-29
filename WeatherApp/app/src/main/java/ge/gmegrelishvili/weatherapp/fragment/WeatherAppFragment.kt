package ge.gmegrelishvili.weatherapp.fragment

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ge.gmegrelishvili.weatherapp.api.ApiClient
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.api.WeatherApiClient
import java.util.*

abstract class WeatherAppFragment : Fragment() {
    companion object {
        const val Am6 = 6 * 60
        const val Pm6 = 18 * 60
        const val FlagIconClicked = "ClickedCapital"

        val apiClient: ApiClient = WeatherApiClient()
    }

    protected lateinit var createdView: View

    abstract fun show(cityName: String)

    fun showToast(value: String) {
        Toast.makeText(requireContext(), value, Toast.LENGTH_LONG).show()
    }

    fun checkTime(date: Date) {
        val c = Calendar.getInstance()
        c.time = date
        val minutes = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE)
        val backColor = if (minutes in Am6 until Pm6) {
            ContextCompat.getColor(requireContext(), R.color.background_day)
        } else {
            ContextCompat.getColor(requireContext(), R.color.background_night)
        }
        createdView.setBackgroundColor(backColor)
    }

}

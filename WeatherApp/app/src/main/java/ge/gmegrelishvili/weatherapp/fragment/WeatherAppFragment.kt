package ge.gmegrelishvili.weatherapp.fragment

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ge.gmegrelishvili.weatherapp.ApiClient
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.WeatherApiClient
import java.util.*

abstract class WeatherAppFragment : Fragment() {
    abstract fun show(cityName: String)

    protected lateinit var createdView: View

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

    companion object {
        const val Am6 = 6 * 60
        const val Pm6 = 18 * 60

        const val argApiClient = "ApiClient"

        val apiClient: ApiClient = WeatherApiClient()
    }
}

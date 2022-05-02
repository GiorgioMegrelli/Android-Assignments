package ge.gmegrelishvili.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class DetailsModel(
    @SerializedName("dt")
    val date: Int,
    val weather: List<WeatherModel>,
    val main: MainModel
) {
    fun getDate(): Date {
        return Date(date * 1000L)
    }
}

package ge.gmegrelishvili.weatherapp.model

import com.google.gson.annotations.SerializedName

data class DetailsModel(
    @SerializedName("dt")
    val date: Int,
    val weather: List<WeatherModel>,
    val main: MainModel
)
package ge.gmegrelishvili.weatherapp.model

import com.google.gson.annotations.SerializedName

data class HourlyInfoModel(
    @SerializedName("dt")
    val date: Int,
    val main: MainModel,
    val weather: List<WeatherModel>
)
package ge.gmegrelishvili.weatherapp.model

import com.google.gson.annotations.SerializedName

data class MainModel(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int
)
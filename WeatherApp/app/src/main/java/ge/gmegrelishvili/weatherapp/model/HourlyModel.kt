package ge.gmegrelishvili.weatherapp.model

import com.google.gson.annotations.SerializedName

data class HourlyModel(
    @SerializedName("list")
    val hourlyData: List<HourlyInfoModel>
)
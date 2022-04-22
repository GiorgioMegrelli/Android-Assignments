package ge.gmegrelishvili.weatherapp

import ge.gmegrelishvili.weatherapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRoute {
    companion object {
        private const val cityNameFieldName = "q"
        private const val apiKeyFieldName = "appid"
        private const val unitsFieldName = "units"

        private const val unitsDefaultValue = "metric"
    }

    @GET("/data/2.5/weather")
    fun getDetails(
        @Query(cityNameFieldName) cityname: String,
        @Query(apiKeyFieldName) apiKey: String,
        @Query(unitsFieldName) units: String = unitsDefaultValue
    ): Call<DetailsModel>

    @GET("/data/2.5/forecast")
    fun getHourly(
        @Query(cityNameFieldName) cityname: String,
        @Query(apiKeyFieldName) apiKey: String,
        @Query(unitsFieldName) units: String = unitsDefaultValue
    ): Call<HourlyModel>
}

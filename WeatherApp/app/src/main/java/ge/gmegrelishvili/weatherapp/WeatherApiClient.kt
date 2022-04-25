package ge.gmegrelishvili.weatherapp

import android.util.Log
import ge.gmegrelishvili.weatherapp.model.DetailsModel
import ge.gmegrelishvili.weatherapp.model.HourlyModel
import ge.gmegrelishvili.weatherapp.security.ApiKeyHolder
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class WeatherApiClient : ApiClient() {
    companion object : Serializable {
        const val BaseUrl = "https://api.openweathermap.org"
    }

    private val apiKey = ApiKeyHolder.ApiKey
    private val httpClient: ApiRoute

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        httpClient = retrofit.create(ApiRoute::class.java)
    }

    override fun getDetails(cityname: String, callback: (Throwable?, DetailsModel?) -> Unit) {
        httpClient.getDetails(cityname, apiKey).enqueue(object : Callback<DetailsModel> {
            override fun onResponse(call: Call<DetailsModel>, response: Response<DetailsModel>) {
                callback(null, response.body())
            }

            override fun onFailure(call: Call<DetailsModel>, t: Throwable) {
                callback(t, null)
            }
        })
    }

    override fun getHourly(cityname: String, callback: (Throwable?, HourlyModel?) -> Unit) {
        httpClient.getHourly(cityname, apiKey).enqueue(object : Callback<HourlyModel> {
            override fun onResponse(call: Call<HourlyModel>, response: Response<HourlyModel>) {
                callback(null, response.body())
            }

            override fun onFailure(call: Call<HourlyModel>, t: Throwable) {
                callback(t, null)
            }
        })
    }
}
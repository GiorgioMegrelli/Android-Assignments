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

class WeatherApiClient : ApiClient {
    companion object {
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

        getDetails("Tbilisi")
        getHourly("Tbilisi")
    }

    override fun getDetails(cityname: String) {
        httpClient.getDetails("Tbilisi", apiKey).enqueue(object : Callback<DetailsModel> {
            override fun onResponse(call: Call<DetailsModel>, response: Response<DetailsModel>) {
                Log.i("MyLogDetails", response.body().toString())
            }

            override fun onFailure(call: Call<DetailsModel>, t: Throwable) {
                Log.i("MyLogDetails", t.toString())
            }
        })
    }

    override fun getHourly(cityname: String) {
        httpClient.getHourly("Tbilisi", apiKey).enqueue(object : Callback<HourlyModel> {
            override fun onResponse(call: Call<HourlyModel>, response: Response<HourlyModel>) {
                Log.i("MyLogHourly", response.body().toString())
            }

            override fun onFailure(call: Call<HourlyModel>, t: Throwable) {
                Log.i("MyLogHourly", t.toString())
            }
        })
    }
}
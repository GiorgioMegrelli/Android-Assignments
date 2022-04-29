package ge.gmegrelishvili.weatherapp.api

import ge.gmegrelishvili.weatherapp.model.DetailsModel
import ge.gmegrelishvili.weatherapp.model.HourlyModel

abstract class ApiClient {
    abstract fun getDetails(cityname: String, callback: (Throwable?, DetailsModel?) -> Unit)

    abstract fun getHourly(cityname: String, callback: (Throwable?, HourlyModel?) -> Unit)
}
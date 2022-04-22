package ge.gmegrelishvili.weatherapp

interface ApiClient {
    fun getDetails(cityname: String)

    fun getHourly(cityname: String)
}
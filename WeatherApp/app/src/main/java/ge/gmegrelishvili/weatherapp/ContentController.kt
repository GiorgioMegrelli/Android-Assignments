package ge.gmegrelishvili.weatherapp

interface ContentController {
    enum class View {
        Today, Hourly
    }

    fun setView(view: View)
}
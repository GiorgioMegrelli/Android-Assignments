package ge.gmegrelishvili.weatherapp

interface ContentController {
    companion object {
        enum class View {
            Today, Hourly
        }
    }

    fun setView(view: View)
}
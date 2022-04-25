package ge.gmegrelishvili.weatherapp

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class WeatherAppContentController(context: AppCompatActivity) : ContentController {
    private val pagerAdapter = context.findViewById<ViewPager2>(R.id.fragment_container)

    init {
        pagerAdapter.adapter = WeatherAppPagerAdapter(context)
    }

    override fun setView(view: ContentController.View) {
        pagerAdapter.currentItem = when (view) {
            ContentController.View.Today -> 0
            ContentController.View.Hourly -> 1
        }
    }
}
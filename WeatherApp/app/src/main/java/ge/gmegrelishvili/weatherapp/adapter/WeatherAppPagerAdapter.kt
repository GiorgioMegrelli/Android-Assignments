package ge.gmegrelishvili.weatherapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.gmegrelishvili.weatherapp.fragment.HourlyFragment
import ge.gmegrelishvili.weatherapp.fragment.TodayFragment

class WeatherAppPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(TodayFragment(), HourlyFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
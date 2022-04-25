package ge.gmegrelishvili.weatherapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ge.gmegrelishvili.weatherapp.fragment.DetailsFragment
import ge.gmegrelishvili.weatherapp.fragment.HourlyFragment

class WeatherAppPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(DetailsFragment(), HourlyFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
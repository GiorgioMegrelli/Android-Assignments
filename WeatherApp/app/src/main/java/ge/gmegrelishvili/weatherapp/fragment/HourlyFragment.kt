package ge.gmegrelishvili.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.weatherapp.Country
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.adapter.HourlyRecyclerViewAdapter
import ge.gmegrelishvili.weatherapp.model.HourlyModel

class HourlyFragment : WeatherAppFragment() {

    companion object {
        const val RequestKey = "HourlyFragmentRequestKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(TodayFragment.RequestKey) { _, bundle ->
            bundle.getString(FlagIconClicked)?.let { show(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createdView = inflater.inflate(R.layout.fragment_hourly, container, false)
        show(Country.CapitalDefault)

        val iconGe = createdView.findViewById<ImageButton>(R.id.flag_icon_georgia)
        val iconUk = createdView.findViewById<ImageButton>(R.id.flag_icon_uk)
        val iconJa = createdView.findViewById<ImageButton>(R.id.flag_icon_jamaica)

        iconGe.setOnClickListener {
            show(Country.CapitalGe)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalGe))
        }
        iconUk.setOnClickListener {
            show(Country.CapitalUk)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalUk))
        }
        iconJa.setOnClickListener {
            show(Country.CapitalJa)
            setFragmentResult(RequestKey, bundleOf(FlagIconClicked to Country.CapitalJa))
        }
        return createdView
    }

    override fun show(cityName: String) {
        val recyclerView = createdView.findViewById<RecyclerView>(R.id.hourly_row_items)
        val adapter = HourlyRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        apiClient.getHourly(cityName) { hourlyError, response ->
            if (hourlyError != null) {
                showToast("Error Occurred with External Server$hourlyError")
            } else {
                createdView.findViewById<TextView>(R.id.hourly_capital_name).text = cityName
                adapter.updateAdapter((response as HourlyModel).hourlyData)
                var itemDecoration =
                    DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)

                requireContext().getDrawable(R.drawable.devider)
                    ?.let { itemDecoration.setDrawable(it) }

                recyclerView.addItemDecoration(itemDecoration)
            }
        }
    }

}
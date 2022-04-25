package ge.gmegrelishvili.weatherapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.gmegrelishvili.weatherapp.Country
import ge.gmegrelishvili.weatherapp.HourlyRecyclerViewAdapter
import ge.gmegrelishvili.weatherapp.R
import ge.gmegrelishvili.weatherapp.model.HourlyModel

class HourlyFragment : WeatherAppFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createdView = inflater.inflate(R.layout.fragment_hourly, container, false)
        show(Country.CapitalDefault)
        return createdView
    }

    override fun show(cityName: String) {
        val adapter = HourlyRecyclerViewAdapter(this)
        val recyclerView = createdView.findViewById<RecyclerView>(R.id.hourly_row_items)
        recyclerView.adapter = adapter

        apiClient.getHourly(cityName) { t, response ->
            if (t != null) {
                showToast("Error Occurred with External Server$t")
            } else {
                response as HourlyModel

                createdView.findViewById<TextView>(R.id.hourly_capital_name).text = cityName
                adapter.items.addAll(response.hourlyData)
                adapter.notifyDataSetChanged()
                recyclerView.addItemDecoration(
                    DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL)
                )
            }
        }
    }
}
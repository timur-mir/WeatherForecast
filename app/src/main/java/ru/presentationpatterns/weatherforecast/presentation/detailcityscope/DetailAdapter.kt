package ru.presentationpatterns.weatherforecast.presentation.detailcityscope

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.presentationpatterns.weatherforecast.databinding.ItemCityTitleBinding
import ru.presentationpatterns.weatherforecast.databinding.ItemWeatherDetailBinding
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo
import ru.presentationpatterns.weatherforecast.presentation.searchscope.CityNameAdapter

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailHolder>() {
    private var weatherCity: List<CityWeatherInfo> = emptyList()
    class DetailHolder (val binding: ItemWeatherDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        return  DetailHolder(
            ItemWeatherDetailBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun getItemCount(): Int {
       return weatherCity.size
    }

    override fun onBindViewHolder(holder: DetailHolder, position: Int) {
        val item =weatherCity[position]
        with(holder.binding) {
            dateObsCity.text = item.date.toString()
            tempObsCity.text=item.tomorrowTempC.toString()
        }
    }
    fun refreshWeather(weatherCity: List<CityWeatherInfo>) {
        this.weatherCity = weatherCity
        notifyDataSetChanged()
    }
}
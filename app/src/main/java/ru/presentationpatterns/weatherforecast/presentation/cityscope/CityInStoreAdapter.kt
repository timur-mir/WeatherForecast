package ru.presentationpatterns.weatherforecast.presentation.cityscope

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.presentationpatterns.weatherforecast.databinding.ItemCityInStoreBinding
import ru.presentationpatterns.weatherforecast.databinding.ItemCityTitleBinding
import ru.presentationpatterns.weatherforecast.presentation.searchscope.CityNameAdapter

class CityInStoreAdapter(private val onClick: (cityName: String) -> Unit) :
    RecyclerView.Adapter<CityInStoreAdapter.CityInStoreNameHolder>() {
    private var citiesInStore: List<String> = emptyList()

    class CityInStoreNameHolder(val binding: ItemCityInStoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityInStoreNameHolder {
        return CityInStoreNameHolder(
            ItemCityInStoreBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun getItemCount(): Int {
        return citiesInStore.size
    }

    override fun onBindViewHolder(holder: CityInStoreNameHolder, position: Int) {
        val item = citiesInStore[position]
        with(holder.binding) {
            cityInStore.text = item.toString()
            holder.binding.cityInStore.setOnClickListener {
                onClick(item.toString())
            }
        }
    }

    fun setCities(citiesInStore: List<String>) {
        this.citiesInStore = citiesInStore
        notifyDataSetChanged()
    }
}
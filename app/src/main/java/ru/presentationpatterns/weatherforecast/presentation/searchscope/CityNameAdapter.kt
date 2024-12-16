package ru.presentationpatterns.weatherforecast.presentation.searchscope

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.presentationpatterns.weatherforecast.R
import ru.presentationpatterns.weatherforecast.databinding.ItemCityTitleBinding

class CityNameAdapter(private val onClick: (townName: String) -> Unit) : RecyclerView.Adapter<CityNameAdapter.TownNameHolder>() {
    private var towns: List<String> = emptyList()

    class TownNameHolder(val binding: ItemCityTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TownNameHolder {
        return TownNameHolder(
            ItemCityTitleBinding
                .inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
        )
    }

    override fun getItemCount(): Int {
        return towns.size
    }

    override fun onBindViewHolder(holder: TownNameHolder, position: Int) {
        val item = towns[position]
        with(holder.binding) {
           townName.text= item.toString()
           townName.setOnClickListener {
                onClick(item.toString())
            }
        }
    }

    fun refreshTown(towns: List<String>) {
        this.towns = towns
        notifyDataSetChanged()
    }
}
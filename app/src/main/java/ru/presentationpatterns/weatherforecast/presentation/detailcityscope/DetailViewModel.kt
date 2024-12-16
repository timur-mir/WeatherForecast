package ru.presentationpatterns.weatherforecast.presentation.detailcityscope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.presentationpatterns.weatherforecast.data.networkrepo.WeatherRepository
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo

class DetailViewModel : ViewModel() {

    private val citiesRepo= WeatherRepository()
    private val cityWeatherDetailFragment: MutableLiveData<List<CityWeatherInfo>> = MutableLiveData()
    val cityWeatherLiveDataDetailFragment: LiveData<List<CityWeatherInfo>>
        get() = cityWeatherDetailFragment
   fun getCityDao(cityName:String) {
       val cities = citiesRepo.getCityAllObj(cityName)
       cityWeatherDetailFragment.postValue(cities)
   }
    suspend fun deleteCity(cityName: String){
        citiesRepo.deleteCity(cityName)
    }
       }



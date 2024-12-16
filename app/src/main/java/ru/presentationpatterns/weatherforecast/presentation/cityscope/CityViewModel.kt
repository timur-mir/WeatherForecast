package ru.presentationpatterns.weatherforecast.presentation.cityscope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.presentationpatterns.weatherforecast.data.networkrepo.WeatherRepository
import ru.presentationpatterns.weatherforecast.data.roomrepo.DataBase
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo

class CityViewModel : ViewModel() {
    private val citiesWeather: MutableLiveData<List<String>> = MutableLiveData()
    val citiesWeatherLiveData: LiveData<List<String>>
        get() = citiesWeather

    private val citiesRepo = WeatherRepository()


    fun getCities() {
        val cities = citiesRepo.getCities()
        citiesWeather.postValue(cities)
    }


}
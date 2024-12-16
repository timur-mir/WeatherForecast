package ru.presentationpatterns.weatherforecast.presentation.searchscope

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ru.presentationpatterns.weatherforecast.data.NeedParamsInfo
import ru.presentationpatterns.weatherforecast.data.networkrepo.NetworkApi
import ru.presentationpatterns.weatherforecast.data.networkrepo.NetworkRepository
import ru.presentationpatterns.weatherforecast.data.networkrepo.WeatherRepository
import ru.presentationpatterns.weatherforecast.data.roomrepo.DataBase
import ru.presentationpatterns.weatherforecast.data.roomrepo.WeatherEntity
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo
import ru.presentationpatterns.weatherforecast.mapToCityWeatherInfo

class SearchViewModel : ViewModel() {
    private val netRepository = NetworkRepository()
    fun getCityWeatherParam(cityName: String,NullResponse:(String?)->Unit) {
        netRepository.getCityWeatherParam(cityName, NullResponse)
    }

    override fun onCleared() {
        super.onCleared()
    }

}
package ru.presentationpatterns.weatherforecast.data.networkrepo

import ru.presentationpatterns.weatherforecast.data.roomrepo.DataBase
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo
import ru.presentationpatterns.weatherforecast.mapToCityWeatherInfo

class WeatherRepository {
    private val weatherCityDao = DataBase.INSTANCE!!.getWeatherCityDao()

     fun getCityAllObj(cityName: String): List<CityWeatherInfo> {
        val filteredByCityName = weatherCityDao.getWeatherCity(cityName)
        return filteredByCityName.map { it.mapToCityWeatherInfo() }
    }
    fun getCities():List<String>{
        return weatherCityDao.getCities()
    }
    suspend fun deleteCity(cityName: String){
        weatherCityDao.deleteOldInfo(cityName)
    }

}

package ru.presentationpatterns.weatherforecast.data.networkrepo

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ru.presentationpatterns.weatherforecast.data.NeedParamsInfo
import ru.presentationpatterns.weatherforecast.data.roomrepo.DataBase
import ru.presentationpatterns.weatherforecast.data.roomrepo.WeatherEntity
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo

class NetworkRepository {
    private var weatherCityDao = DataBase.INSTANCE!!.getWeatherCityDao()
    fun getCityWeatherParam(cityName: String,nullResponse:(String?)->Unit) {
        val getInfoForUrl = " https://api.weatherapi.com/v1/forecast.json?key=${API_KEY}&q=${cityName}" +
                "&days=3&aqi=no&alerts=no"
        CoroutineScope(Dispatchers.IO).launch  {
            val responseString = NetworkApi.getInstance()?.APIrequest(getInfoForUrl)
           // withContext(Dispatchers.Main) {
                delay(300)
                if(responseString!=null) {
                    val needParams = parseJsonString(responseString.toString())
                    val cityWeatherRoom =
                        WeatherEntity(0, needParams.city, needParams.currentTempC, needParams.tomorrowTempC,needParams.date)
                    weatherCityDao.insertWeather(cityWeatherRoom)

                    val cityParams =
                        CityWeatherInfo(
                            0,
                            needParams.city,
                            needParams.currentTempC,
                            needParams.tomorrowTempC,
                            needParams.date
                        )
                    Log.d("MyLog", "Response:${cityParams.toString()}")
                    nullResponse("Ответ есть")
                }
                else
                {
                    nullResponse(null)
                }
        //    }
        }
    }
    fun parseJsonString(str: String): NeedParamsInfo {
        val jsonOb = JSONObject(str)
        Log.d("MyLog2", "Response:${jsonOb.toString()}")
        val nameObj = jsonOb.getJSONObject("location")
        val cityName = nameObj.getString("name")
        val tempObj = jsonOb.getJSONObject("current")
        val temp = tempObj.getDouble("temp_c")
        var forecastObj = jsonOb.getJSONObject("forecast")
        var forecastArray = forecastObj.getJSONArray("forecastday")
        var forecastFirstEl = forecastArray.getJSONObject(0)
        var forecastDay=forecastFirstEl.getString("date")
        var forecastSecondEl=forecastArray.getJSONObject(1)
        var forecastHourArray=forecastSecondEl.getJSONArray("hour")
        var forecast2DayHourObject=forecastHourArray.getJSONObject(13)
        var forecast2Day=forecastSecondEl.getString("date")
        var tommorowTemp=forecast2DayHourObject.getDouble("temp_c")
        Log.d("MyLog3", "Response:${forecast2Day.toString()} ${forecast2DayHourObject.getDouble("temp_c").toString()}")

        return NeedParamsInfo(
            cityName,
            temp,
            tommorowTemp,
            forecast2Day
        )

    }
    companion object {
        const val API_KEY = "92fffa5725f24428918111715230212"

    }
}
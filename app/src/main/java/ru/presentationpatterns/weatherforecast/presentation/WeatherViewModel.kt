package ru.presentationpatterns.weatherforecast.presentation

import androidx.lifecycle.ViewModel


class WeatherViewModel : ViewModel() {
//    private val weatherCityDao = DataBase.INSTANCE!!.getWeatherCityDao()
//    var paramBundle: MutableLiveData<NeedParamsInfo> = MutableLiveData(null)
//    private val cityWeather: MutableLiveData<List<CityWeatherInfo>> = MutableLiveData()
//    val cityWeatherLiveData: LiveData<List<CityWeatherInfo>>
//        get() = cityWeather
//
//  fun saveCity(cityWeatherInfo: WeatherEntity) {
//      viewModelScope.launch(Dispatchers.IO) {
//          weatherCityDao.insertWeather(cityWeatherInfo)
//      }
//  }
//   suspend fun getCity(cityName:String)= withContext(Dispatchers.IO){
//       weatherCityDao.getWeatherCity(cityName).map{it.mapToCityWeatherInfo()}
//   }
//
//
//
//
// fun getCities()=weatherCityDao.getCities()
//    fun getCityWeatherParam(urlStr: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val responseString = NetworkApi.getInstance()?.APIrequest(urlStr)
//            withContext(Dispatchers.Main) {
//                paramBundle.value = parseJsonString(responseString.toString())
//                val infoForLog = parseJsonString(responseString.toString())
//                Log.d("MyLog", "Response:${infoForLog}")
//            }
//        }
//    }
//
//    fun parseJsonString(str: String): NeedParamsInfo {
//        val jsonOb = JSONObject(str)
//        val nameObj = jsonOb.getJSONObject("location")
//        val cityName = nameObj.getString("name")
//        val tempObj = jsonOb.getJSONObject("current")
//        val temp = tempObj.getInt("temp_c")
//        var forecastObj = jsonOb.getJSONObject("forecast")
//        var forecastArray = forecastObj.getJSONArray("forecastday")
//        var forecastFirstEl = forecastArray.getJSONObject(0)
//        var forecastDay=forecastFirstEl.getString("date")
//
//    return NeedParamsInfo(
//        cityName,
//        temp,
//        forecastDay
//            )
//
//    }

}
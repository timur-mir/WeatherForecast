package ru.presentationpatterns.weatherforecast

import ru.presentationpatterns.weatherforecast.data.roomrepo.WeatherEntity
import ru.presentationpatterns.weatherforecast.domain.CityWeatherInfo

fun WeatherEntity.mapToCityWeatherInfo()=
    CityWeatherInfo(
        id,
        cityName,
        currentTempC,
        tomorrowTempC,
        date
    )
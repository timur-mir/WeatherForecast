package ru.presentationpatterns.weatherforecast.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityWeatherInfo (
    var id:Int,
    val cityName: String,
    val currentTempC:Double,
    val tomorrowTempC:Double,
    val date: String
        ): Parcelable
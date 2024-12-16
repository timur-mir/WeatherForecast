package ru.presentationpatterns.weatherforecast.data.roomrepo

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    val cityName: String,
    val currentTempC:Double,
    val tomorrowTempC:Double,
    val date: String
)

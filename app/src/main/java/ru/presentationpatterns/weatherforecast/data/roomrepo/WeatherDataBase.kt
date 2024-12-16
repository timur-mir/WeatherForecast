package ru.presentationpatterns.weatherforecast.data.roomrepo

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.presentationpatterns.weatherforecast.data.roomrepo.WeatherDataBase.Companion.DB_VERSION

@Database(
    entities = [WeatherEntity::class],
    version = DB_VERSION
)

abstract class WeatherDataBase :RoomDatabase(){
    abstract fun getWeatherCityDao() :WeatherCityDateDao

    companion object {
        const val DB_VERSION = 2
        const val DB_NAME = "WeatherBase"
    }
}
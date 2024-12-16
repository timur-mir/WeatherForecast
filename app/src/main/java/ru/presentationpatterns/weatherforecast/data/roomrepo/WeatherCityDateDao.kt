package ru.presentationpatterns.weatherforecast.data.roomrepo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherCityDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(cityInfoWeather: WeatherEntity)

    @Query("SELECT * FROM WeatherEntity WHERE cityName LIKE :city")
    fun getWeatherCity(city: String): List<WeatherEntity>

    @Query("SELECT * FROM WeatherEntity ")
    fun getAllObject():List<WeatherEntity>

    @Query("DELETE FROM WeatherEntity WHERE cityName = :city")
    suspend fun deleteOldInfo(city:String)

    @Query("SELECT DISTINCT cityName FROM WeatherEntity")
   fun getCities() :List<String>
}
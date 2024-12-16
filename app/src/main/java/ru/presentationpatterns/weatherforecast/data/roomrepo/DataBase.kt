package ru.presentationpatterns.weatherforecast.data.roomrepo

import android.content.Context
import androidx.room.Room

object DataBase {
    var INSTANCE: WeatherDataBase? = null
        private set

    fun initDatabase(context: Context) {
        if (INSTANCE == null) {
            synchronized(WeatherDataBase::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        WeatherDataBase::class.java,
                        WeatherDataBase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                }
            }
        }
    }
}
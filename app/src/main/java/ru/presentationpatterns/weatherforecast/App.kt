package ru.presentationpatterns.weatherforecast

import android.app.Application
import android.content.Context
import ru.presentationpatterns.weatherforecast.data.networkrepo.NetworkApi
import ru.presentationpatterns.weatherforecast.data.roomrepo.DataBase

class App : Application() {
    companion object {
        var appContext: Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        DataBase.initDatabase(this)
        NetworkApi.getInstance(this)
        appContext = applicationContext
    }
}
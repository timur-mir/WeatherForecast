package ru.presentationpatterns.weatherforecast.data


data class NeedParamsInfo(
    var city: String,
    val currentTempC:Double,
    val tomorrowTempC:Double,
    var date: String


)


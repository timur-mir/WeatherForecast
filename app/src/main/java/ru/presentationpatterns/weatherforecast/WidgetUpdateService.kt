package ru.presentationpatterns.weatherforecast

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import ru.presentationpatterns.weatherforecast.MainActivity.Panel.currentPlace

class WidgetUpdateService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val allWidgetIds = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
        if (allWidgetIds != null) {
            for (appWidgetId in allWidgetIds) {
        WeatherWidget.updateAppWidget(this, appWidgetManager, appWidgetId, MainActivity.Panel.temperature,currentPlace)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
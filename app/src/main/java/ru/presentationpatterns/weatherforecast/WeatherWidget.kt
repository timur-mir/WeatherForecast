package ru.presentationpatterns.weatherforecast

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.widget.RemoteViews
import androidx.core.os.persistableBundleOf
import ru.presentationpatterns.weatherforecast.MainActivity.Panel.searchPlaсe

class WeatherWidget : AppWidgetProvider() {
    var widgetText = ""
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val intent = Intent(context.applicationContext, WidgetUpdateService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        context.startService(intent)
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    companion object {

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            temp: Int,
            currentTown: String
        ) {
            var widgetText = ""
            if (searchPlaсe.isEmpty()) {
                if (temp == 1 || temp == 21 || temp == 31 || temp == 41 || temp == 51 || temp == 61 || temp == 71 || temp == 81) {
                    widgetText =
                        "$currentTown, ${context.getString(R.string.appwidget_text)}  $temp градус"
                } else if (temp == 2 || temp == 3 || temp == 4 || temp == 22 || temp == 23 || temp == 24 || temp == 32 || temp == 33 || temp == 34
                    || temp == 42 || temp == 43 || temp == 44 || temp == 52 || temp == 53 || temp == 54 || temp == 62 || temp == 63 || temp == 64
                    || temp == 72 || temp == 73 || temp == 74
                ) {
                    widgetText =
                        "$currentTown, ${context.getString(R.string.appwidget_text)}  $temp градуса"
                } else {
                    widgetText =
                        "$currentTown, ${context.getString(R.string.appwidget_text)}  $temp градусов"
                }
            } else {
                if (temp == 1 || temp == 21 || temp == 31 || temp == 41 || temp == 51 || temp == 61 || temp == 71 || temp == 81) {
                    widgetText =
                        "$searchPlaсe, ${context.getString(R.string.appwidget_text_after_search)}  $temp градус"
                } else if (temp == 2 || temp == 3 || temp == 4 || temp == 22 || temp == 23 || temp == 24 || temp == 32 || temp == 33 || temp == 34
                    || temp == 42 || temp == 43 || temp == 44 || temp == 52 || temp == 53 || temp == 54 || temp == 62 || temp == 63 || temp == 64
                    || temp == 72 || temp == 73 || temp == 74
                ) {
                    widgetText =
                        "$searchPlaсe, ${context.getString(R.string.appwidget_text_after_search)}  $temp градуса"
                } else {
                    widgetText =
                        "$searchPlaсe, ${context.getString(R.string.appwidget_text_after_search)}  $temp градусов"
                }
            }
            val views = RemoteViews(context.packageName, R.layout.weather_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
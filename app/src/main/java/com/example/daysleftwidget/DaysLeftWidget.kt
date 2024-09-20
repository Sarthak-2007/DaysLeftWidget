package com.example.daysleftwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.math.abs

class DaysLeftWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_days_left)
            
            val dates = loadDates(context)
            if (dates.isNotEmpty()) {
                val dateItem = dates.last() // Use the last added date
                val daysLeft = getDaysLeft(dateItem.date)
                views.setTextViewText(R.id.daysLeftText, "$daysLeft days left")
                views.setTextViewText(R.id.labelText, dateItem.label)
            } else {
                views.setTextViewText(R.id.daysLeftText, "No dates set")
                views.setTextViewText(R.id.labelText, "")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getDaysLeft(targetDate: Long): Int {
            val today = Calendar.getInstance().timeInMillis
            return abs(((targetDate - today) / (1000 * 60 * 60 * 24)).toInt())
        }

        private fun loadDates(context: Context): List<DateItem> {
            val prefs = context.getSharedPreferences("DatePrefs", Context.MODE_PRIVATE)
            val json = prefs.getString("dates", "[]")
            return Gson().fromJson(json, object : TypeToken<List<DateItem>>() {}.type)
        }
    }
}

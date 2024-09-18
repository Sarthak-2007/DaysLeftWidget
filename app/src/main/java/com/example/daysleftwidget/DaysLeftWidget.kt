package com.example.daysleftwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
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
            val daysLeft = getDaysLeft(context)
            views.setTextViewText(R.id.daysLeftText, daysLeft.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getDaysLeft(context: Context): Int {
            val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
            val targetDate = prefs.getLong("TargetDate", 0)
            if (targetDate == 0L) {
                // If no date is set, default to end of year
                val endOfYear = Calendar.getInstance().apply {
                    set(Calendar.MONTH, Calendar.DECEMBER)
                    set(Calendar.DAY_OF_MONTH, 31)
                }
                return ((endOfYear.timeInMillis - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
            }
            return abs(((targetDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt())
        }
    }
}

package com.example.daysleftwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.util.*

class DaysLeftWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_days_left)
            val daysLeft = getDaysLeftInYear()
            views.setTextViewText(R.id.daysLeftText, daysLeft.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getDaysLeftInYear(): Int {
            val today = Calendar.getInstance()
            val endOfYear = Calendar.getInstance()
            endOfYear.set(Calendar.MONTH, Calendar.DECEMBER)
            endOfYear.set(Calendar.DAY_OF_MONTH, 31)
            return ((endOfYear.timeInMillis - today.timeInMillis) / (1000 * 60 * 60 * 24)).toInt() + 1
        }
    }
}

package com.example.daysleftwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.util.*
import kotlin.math.abs

class DaysLeftWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelUpdate(context)
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_days_left)
            val prefs = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
            val targetDate = prefs.getLong("TargetDate_$appWidgetId", 0)
            val customText = prefs.getString("CustomText_$appWidgetId", "")

            if (targetDate != 0L) {
                val daysLeft = getDaysLeft(targetDate)
                views.setTextViewText(R.id.daysLeftText, "$daysLeft days left")
                views.setTextViewText(R.id.customText, customText)
            } else {
                views.setTextViewText(R.id.daysLeftText, "Set a date")
                views.setTextViewText(R.id.customText, "")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getDaysLeft(targetDate: Long): Int {
            return abs(((targetDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt())
        }

        private fun scheduleUpdate(context: Context) {
            val intent = Intent(context, DaysLeftWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
        }

        private fun cancelUpdate(context: Context) {
            val intent = Intent(context, DaysLeftWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }
}

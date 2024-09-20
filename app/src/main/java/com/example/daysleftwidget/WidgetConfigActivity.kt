package com.example.daysleftwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WidgetConfigActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(Activity.RESULT_CANCELED)

        // Find the widget id from the intent.
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val labelEditText = findViewById<EditText>(R.id.labelEditText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        confirmButton.setOnClickListener {
            val label = labelEditText.text.toString()
            val dates = loadDates()
            if (dates.isNotEmpty()) {
                val dateItem = dates.last() // Use the last added date
                updateWidget(dateItem, label)
                setResult(Activity.RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId))
                finish()
            }
        }
    }

    private fun updateWidget(dateItem: DateItem, label: String) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val dates = loadDates()
        DaysLeftWidget.updateAppWidget(this, appWidgetManager, appWidgetId, dates)
    }

    private fun loadDates(): List<DateItem> {
        val prefs = getSharedPreferences("DatePrefs", MODE_PRIVATE)
        val json = prefs.getString("dates", "[]")
        return Gson().fromJson(json, object : TypeToken<List<DateItem>>() {}.type)
    }
}

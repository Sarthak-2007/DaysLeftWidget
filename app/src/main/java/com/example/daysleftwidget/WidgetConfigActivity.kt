package com.example.daysleftwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import java.util.*

class WidgetConfigActivity : Activity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_config)

        widgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val customTextInput = findViewById<EditText>(R.id.customTextInput)
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val customText = customTextInput.text.toString()

            val targetDate = Calendar.getInstance().apply {
                set(year, month, day)
            }

            getSharedPreferences("WidgetPrefs", MODE_PRIVATE).edit().apply {
                putLong("TargetDate_$widgetId", targetDate.timeInMillis)
                putString("CustomText_$widgetId", customText)
                apply()
            }

            val appWidgetManager = AppWidgetManager.getInstance(this)
            DaysLeftWidget.updateAppWidget(this, appWidgetManager, widgetId)

            val resultValue = Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            setResult(RESULT_OK, resultValue)
            finish()
        }
    }
}

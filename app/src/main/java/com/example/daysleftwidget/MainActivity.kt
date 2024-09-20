package com.example.daysleftwidget

import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var datesList: MutableList<DateItem>
    private lateinit var adapter: DateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datesList = loadDates()
        adapter = DateListAdapter(this, datesList)

        val listView = findViewById<ListView>(R.id.datesList)
        listView.adapter = adapter

        val addButton = findViewById<Button>(R.id.addDateButton)
        addButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val date = Calendar.getInstance()
                date.set(year, month, day)
                showLabelDialog(date.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showLabelDialog(dateInMillis: Long) {
        val input = EditText(this)
        input.hint = "Enter label"
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Enter Label")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val label = input.text.toString()
                addDate(dateInMillis, label)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addDate(dateInMillis: Long, label: String) {
        val newItem = DateItem(dateInMillis, label)
        datesList.add(newItem)
        saveDates()
        adapter.notifyDataSetChanged()
        updateWidgets()
    }

    private fun loadDates(): MutableList<DateItem> {
        val prefs = getSharedPreferences("DatePrefs", MODE_PRIVATE)
        val json = prefs.getString("dates", "[]")
        return Gson().fromJson(json, object : TypeToken<MutableList<DateItem>>() {}.type)
    }

    private fun saveDates() {
        val prefs = getSharedPreferences("DatePrefs", MODE_PRIVATE)
        val json = Gson().toJson(datesList)
        prefs.edit().putString("dates", json).apply()
    }

    private fun updateWidgets() {
        val intent = Intent(this, DaysLeftWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, DaysLeftWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}

data class DateItem(val date: Long, val label: String)

package com.example.daysleftwidget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DateListAdapter(context: Context, private val dates: List<DateItem>) : ArrayAdapter<DateItem>(context, 0, dates) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.date_list_item, parent, false)
        
        val item = dates[position]
        itemView.findViewById<TextView>(R.id.dateText).text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(item.date))
        itemView.findViewById<TextView>(R.id.labelText).text = item.label

        return itemView
    }
}

package com.bavuchoko.jsparkgolf.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bavuchoko.jsparkgolf.R

class CitySpinnerAdapter (context: Context, items: List<String>):
    ArrayAdapter<String>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
            val itemTextView = view.findViewById<TextView>(android.R.id.text1)
            itemTextView.text = getItem(position)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.city_dropdown, parent, false)
            val itemTextView = view.findViewById<TextView>(R.id.text_item)
            itemTextView.text = getItem(position)
            return view
        }
}
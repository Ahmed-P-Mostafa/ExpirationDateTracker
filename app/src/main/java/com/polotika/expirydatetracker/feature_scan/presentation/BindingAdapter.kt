package com.polotika.expirydatetracker.feature_scan.presentation

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*


@BindingAdapter("setListener")
fun setListener(view: AutoCompleteTextView, listener: AdapterView.OnItemClickListener) {
    view.onItemClickListener = listener
    val list = listOf("6 Hours", "12 Hours", "18 Hours", "24 Hours")
    val adapter = ArrayAdapter(view.context, android.R.layout.simple_dropdown_item_1line, list)
    view.setAdapter(adapter)
}


@BindingAdapter("android:convertItemDate")
fun convertItemDate(textView: TextView, date: Long) {
    val time = (date - Calendar.getInstance().timeInMillis) / 1000 / 60
    val minutes = time % 60
    val hours = time / 60
    textView.text = "valid until: $hours:$minutes  hours "


}

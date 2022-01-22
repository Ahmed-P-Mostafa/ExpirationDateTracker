package com.polotika.expirydatetracker.feature_scan.presentation

import android.R
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class BindingAdapter {

    companion object{
        @JvmStatic
        @BindingAdapter("valueAttrChanged")
        fun AutoCompleteTextView.setListener(listener: InverseBindingListener?) {
            this.onItemSelectedListener = if (listener != null) {
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        listener.onChange()
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        listener.onChange()
                    }
                }
            } else {
                null
            }
        }


        @JvmStatic
        @BindingAdapter("entries", "itemLayout", "textViewId", requireAll = false)
        fun AutoCompleteTextView.bindAdapter(entries: Array<String>, @LayoutRes itemLayout: Int?, @IdRes textViewId: Int?) {
            val adapter = when {
                itemLayout == null -> {
                    ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, entries)
                }
                textViewId == null -> {
                    ArrayAdapter(context, itemLayout, entries)
                }
                else -> {
                    ArrayAdapter(context, itemLayout, textViewId, entries)
                }
            }
            setAdapter(adapter)
        }
    }

    @BindingAdapter("android:convertItemDate")
    fun convertItemDate(textView: TextView, date: Long) {
        val timeFormat = SimpleDateFormat("kk-mm").format(date)
        val d = Calendar.getInstance()
        d.timeInMillis = date

        textView.text = timeFormat.format(d.time)
    }

    @BindingAdapter("scanProduct")
    fun scanProduct(floatingActionButton: FloatingActionButton, value: Boolean) {
        while (value) {
            floatingActionButton.setOnClickListener {

            }
        }
    }


    @BindingAdapter("android:datesAdapter")
    fun AutoCompleteTextView.datesAdapter(value: Boolean) {
        while (value) {
                val periods = arrayOf("6 Hours", "12 Hours", "18 Hours", "24 Hours")
            val arrayAdapter =
                ArrayAdapter<String>(context, R.layout.simple_dropdown_item_1line, periods)
            //(view.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
        }
    }


}


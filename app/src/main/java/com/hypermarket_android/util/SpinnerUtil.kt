package com.hypermarket_android.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.hypermarket_android.R

object SpinnerUtil {
    //  Set item in spinner according to the position
    fun setItemAtPosition(spinner: Spinner, array: List<String>, value: String) {
        for (i in array.indices) {
            if (array[i].equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                break
            } else
                spinner.setSelection(array.size - 1)
        }
    }


    //set the data to the spinner
    fun setSpinner(spinner: Spinner, array: List<String>, context: Context) {

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context, R.layout.sort_by_layout, array)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_layout)

        // attaching data adapter to spinner
        spinner.adapter = dataAdapter
    }
}
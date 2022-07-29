package com.hypermarket_android.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hypermarket_android.R
import com.hypermarket_android.ui.home.SpinnerModel

class CustomDropDownAdapter(val context: Context, var listItemsTxt: ArrayList<SpinnerModel>) : BaseAdapter() {
    val mInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.view_drop_down_menu, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        // setting adapter item height programatically.
        val params = view.layoutParams
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        view.layoutParams = params
        if(position==0){
            vh.label.hint = listItemsTxt[position].name
            vh.label.setTextColor(ContextCompat.getColor(context,R.color.black))
            vh.label.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
        }else{
            vh.label.text =  listItemsTxt[position].name
            vh.label.setTextColor(ContextCompat.getColor(context,R.color.black))
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = null
        if (position === 0) {
            val tv = TextView(context)
            tv.visibility = View.GONE

            v = tv
        } else {
            v = super.getDropDownView(position, null, parent)
        }
        return v!!
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = listItemsTxt.size

    private class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.txtDropDownLabel) as TextView
    }


}
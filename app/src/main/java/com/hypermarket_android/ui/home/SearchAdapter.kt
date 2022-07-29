package com.hypermarket_android.ui.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R


 class SearchAdapter(private var names: ArrayList<String>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_search_layout_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.text = names[position]
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView

        init {
            textViewName = itemView.findViewById(R.id.textViewName)
        }
    }

     fun filterList(filterdNames: ArrayList<String>) {
         names = filterdNames
         notifyDataSetChanged()
     }

 }
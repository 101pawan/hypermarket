package com.hypermarket_android.nestedrecycler.adapter

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.nestedrecycler.model.ChildItem


class ChildItemAdapter(
    private val context: FragmentActivity?,
    private val childItemList: List<ChildItem>
) :
    RecyclerView.Adapter<ChildItemAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ChildItemTitle = itemView.findViewById<TextView>(com.hypermarket_android.R.id.child_item_title)

    }


    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(com.hypermarket_android.R.layout.child_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return childItemList.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val childItem = childItemList!![position]

        // For the created instance, set title.
        // No need to set the image for
        // the ImageViews because we have
        // provided the source for the images
        // in the layout file itself
        holder.ChildItemTitle.text = childItem.getChildItemTitle()
      }
}

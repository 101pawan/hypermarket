package com.app.pharmadawa.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.ui.home.Stores
import kotlinx.android.synthetic.main.stores_layout.view.*

class StoresRecyclerAdapter(
    private val context: FragmentActivity?,
    private var storesList: ArrayList<Stores>
) :
    RecyclerView.Adapter<StoresRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.stores_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return storesList.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.apply {

          //  Picasso.with(context).load(photo.url).into(trending_image)
            img_stores.setImageResource(storesList[position].imageIcon)

        }
    }
}
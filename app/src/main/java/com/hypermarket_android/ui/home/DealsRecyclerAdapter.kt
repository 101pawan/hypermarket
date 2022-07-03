package com.app.pharmadawa.ui.notification

import android.app.Activity
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.DealsListActivity
import com.hypermarket_android.dataModel.DealsDataModel
import kotlinx.android.synthetic.main.deals_layout.view.*

class DealsRecyclerAdapter(
    private val activity: Activity?,
    private var dealsList: ArrayList<DealsDataModel.DealsData>
) :
    RecyclerView.Adapter<DealsRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.deals_layout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return dealsList.size
    }

    //Bind View Holder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

     //   Glide.with(context!!).load(dealsList[position].deal_image).into(d)
        holder.itemView.apply {

            val displayMetrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels-80
           // width=width
            deals_image.layoutParams.width=width/2
            deals_image.requestLayout()

            Glide.with(context!!).load(dealsList[position].deal_image).into(deals_image)

            deals_image.setOnClickListener {
                context.startActivity(Intent(context!!,DealsListActivity::class.java).putExtra("id",dealsList[position].id).putExtra("name",dealsList.get(position).deal_name))
            }

//            Picasso.with(context).load(photo.url).into(trending_image)
//            deals_image.setImageResource(dealsList[position].image)
//            tv_discounts.text = dealsList[position].discount
//            tv_prev_price.text = dealsList[position].prev_price
//            tv_next_price.text = dealsList[position].next_price
//
//            tv_prev_price.paintFlags = tv_prev_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        }
    }
}
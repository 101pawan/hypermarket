package com.app.pharmadawa.ui.notification

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.DealsListActivity
import com.hypermarket_android.dataModel.DealsDataModel
import kotlinx.android.synthetic.main.item_tab_deals_layout.view.*

class DealsTabRecyclerAdapter(
    private val context: FragmentActivity?,
    private var dealsList: ArrayList<DealsDataModel.DealsData>
) :
    RecyclerView.Adapter<DealsTabRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_tab_deals_layout, p0, false)
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

            Glide.with(context!!).load(dealsList[position].deal_image).into(deal_image)

            deal_image.setOnClickListener {
                context.startActivity(
                    Intent(
                        context!!,
                        DealsListActivity::class.java
                    ).putExtra("id", dealsList[position].id).putExtra("name", dealsList[position].deal_name)
                )
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
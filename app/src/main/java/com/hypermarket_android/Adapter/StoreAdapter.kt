package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.StoreInfoActivity
import com.hypermarket_android.dataModel.StoreDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.SharedPreferenceUtil


class StoreAdapter(
    private var isHome: Boolean,
    private var listOfStore: List<StoreDataModel.StoreData>,
    var activity: Activity
) :
    RecyclerView.Adapter<StoreAdapter.ItemViewHolder>() {
    private var recyclerViewClickListener: RecyclerViewClickListener? = null


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout = itemView.findViewById<CardView>(R.id.cv_parent_layout)
        val rlSelectedView = itemView.findViewById<RelativeLayout>(R.id.rl_selected_view)
        val ivStoreInfo = itemView.findViewById<ImageView>(R.id.iv_info)
        var ivStorePhoto = itemView.findViewById<ImageView>(R.id.iv_store_image)
        var tvStoreName = itemView.findViewById<TextView>(R.id.tv_store_name)
        var tvStoreAddress = itemView.findViewById<TextView>(R.id.tv_store_address)
        var rbStoreRating = itemView.findViewById<RatingBar>(R.id.simpleRatingBar)


        init {

            getItem(0).isSelected=true
            notifyDataSetChanged()


            /*      parentLayout.setOnClickListener { v ->
                      recyclerViewClickListener!!.onClick(R.id.cv_parent_layout, adapterPosition)

                      if (!isSelected) {
                          isSelected = true
                          rlSelectedView.visibility = View.VISIBLE

                      } else {
                          isSelected = false
                          rlSelectedView.visibility = View.GONE
                      }
                      *//* if (!listOfStore[adapterPosition].isSelected) {
                     listOfStore[adapterPosition].isSelected = true
                 } else {
                     listOfStore[adapterPosition].isSelected = false
                 }*//*

                notifyDataSetChanged()
            }*/

            ivStoreInfo.setOnClickListener {
                activity.startActivity(
                    Intent(
                        activity,
                        StoreInfoActivity::class.java
                    ).putExtra("storeId", getItem(adapterPosition).id)
                )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_store_layout, parent, false)

        return ItemViewHolder(itemView)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val sharedPreference = SharedPreferenceUtil.getInstance(activity)

        if (isHome) {

            for (i in 0 until listOfStore.size) {

                if (sharedPreference.storeId == listOfStore[i].id) {
                    listOfStore.get(i).isSelected = true

                } else {
                    listOfStore.get(i).isSelected = false

                }
            }
        }
        if (listOfStore[position].isSelected) {
            holder.rlSelectedView.visibility = View.VISIBLE
        } else {
            holder.rlSelectedView.visibility = View.GONE
        }

        holder.tvStoreName.setText(getItem(position).name)
        holder.tvStoreAddress.setText(getItem(position).address)
        //  holder.tvStoreAddress.setText(getItem(position).formatted_address)
        holder.rbStoreRating.rating = getItem(position).rating.toFloat()
        Glide.with(activity).load(getItem(position).image)
            .placeholder(R.drawable.no_image)
            .into(holder.ivStorePhoto)


        holder.parentLayout.setOnClickListener { v ->

            isHome = false
            if (listOfStore.get(position).isSelected) {
                listOfStore.get(position).isSelected = false
                recyclerViewClickListener!!.onClick(
                    R.id.cv_parent_layout,
                    position
                )


            } else {
                for (store in listOfStore) {
                    store.isSelected = false
                }
                listOfStore.get(position).isSelected = true
                recyclerViewClickListener!!.onClick(
                    R.id.cv_parent_layout,
                    position
                )


            }
            notifyDataSetChanged()

        }


    }


    override fun getItemCount(): Int {
        return listOfStore.size
    }

    fun getItem(position: Int): StoreDataModel.StoreData {
        return listOfStore.get(position)
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }


}
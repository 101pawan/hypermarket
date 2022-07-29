package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.ProductDetailPhotoListAdapter
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import android.graphics.drawable.GradientDrawable
import android.transition.CircularPropagation
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class ProductColorListAdapter(private val context: Activity?,
                              private var listOfImage: ArrayList<ProductDetailDataModel.ProductData.MesurementUnit.ColourInfo>
) :
    RecyclerView.Adapter<ProductColorListAdapter.MyViewHolder>() {
    private var recyclerViewClickListener: RecyclerViewClickListener?=null
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgColor = itemView.findViewById<CircleImageView>(R.id.color_img)
        var relColor = itemView.findViewById<RelativeLayout>(R.id.color_relative)
        init {
            relColor.setOnClickListener {
                if (listOfImage.get(adapterPosition).isSelected) {
                    listOfImage.get(adapterPosition).isSelected = true
                } else {
                    for (image in listOfImage) {
                        image.isSelected = false
                    }
                    listOfImage.get(adapterPosition).isSelected = true
                }
                if (recyclerViewClickListener != null){
                    recyclerViewClickListener!!.onClick(
                        R.id.color_relative,
                        adapterPosition
                    )
                }

                notifyDataSetChanged()
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_product_color_list, p0, false)
        return MyViewHolder(view)
    }
    //Return size
    override fun getItemCount(): Int {
        return listOfImage.size
    }
    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listOfImage[position].images != null && listOfImage[position].images!!.size > 0){
           Glide.with(context!!).load(listOfImage[position].images!![0].images)
               .placeholder(R.drawable.no_image).into(holder.imgColor)
        }
        //holder.viewColor.setBackgroundColor(context?.resources?.getColor(R.color.blue)!!)
        if (getItem(position).isSelected){
            holder.imgColor.borderColor = context?.resources?.getColor(R.color.orange_btn)!!
        }else{
            holder.imgColor.borderColor = context?.resources?.getColor(R.color.color_text_9F9F9F)!!
        }
    }

    fun getItem(postion: Int): ProductDetailDataModel.ProductData.MesurementUnit.ColourInfo {
        return listOfImage[postion]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }
    fun updateListItems(listItems:ArrayList<ProductDetailDataModel.ProductData.MesurementUnit.ColourInfo>){
        listOfImage = listItems
        notifyDataSetChanged()
    }
}
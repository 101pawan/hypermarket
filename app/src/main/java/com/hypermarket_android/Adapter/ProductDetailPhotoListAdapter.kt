package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.listener.RecyclerViewClickListener
import de.hdodenhof.circleimageview.CircleImageView

class ProductDetailPhotoListAdapter(
    private val context: Activity?,
    private var listOfImage: ArrayList<ProductDetailDataModel.ProductData.MesurementUnit>
) :
    RecyclerView.Adapter<ProductDetailPhotoListAdapter.MyViewHolder>() {
    private var recyclerViewClickListener:RecyclerViewClickListener?=null
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSize = itemView.findViewById<TextView>(R.id.tv_size)
        var relSize = itemView.findViewById<RelativeLayout>(R.id.rel_size)
        init {
            relSize.setOnClickListener {
                if (listOfImage.get(adapterPosition).isSelected) {
                    listOfImage.get(adapterPosition).isSelected = true

                } else {
                    for (image in listOfImage) {
                        image.isSelected = false
                    }
                    listOfImage.get(adapterPosition).isSelected = true
                }
                recyclerViewClickListener!!.onClick(
                    R.id.rel_size,
                    adapterPosition
                )

                notifyDataSetChanged()
            }
        }

    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_product_size_list, p0, false)
        return MyViewHolder(view)
    }
    //Return size
    override fun getItemCount(): Int {
        return listOfImage.size
    }
    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvSize.text = listOfImage[position].measurement_unit
        if (getItem(position).isSelected){
            holder.relSize.background = context?.resources?.getDrawable(R.drawable.size_back_selected)
        }else{
            holder.relSize.background = context?.resources?.getDrawable(R.drawable.size_back_unselected)
        }
    }

    fun getItem(postion: Int): ProductDetailDataModel.ProductData.MesurementUnit {
        return listOfImage[postion]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }
}
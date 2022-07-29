package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.OrderListResponse
import kotlinx.android.synthetic.main.products_order_item_view.view.*
import kotlin.collections.ArrayList

class ProductsOrderItemAdapter(
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<OrderListResponse.OrderData>
) :
    RecyclerView.Adapter<ProductsOrderItemAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.products_order_item_view, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tv_rating.text = listOfOrder.get(position).rating ?: ""
        holder.itemView.simpleRatingBar.rating = listOfOrder.get(position).rating!!.toFloat()
        if(listOfOrder.get(position).product_name!!.isEmpty()){
            holder.itemView.tv_name.text = "NA"
        }else{
            holder.itemView.tv_name.text = listOfOrder.get(position).name ?: ""
            holder.itemView.tv_selling_price.text =
                listOfOrder.get(position).product_price + context?.resources?.getString(R.string.aed)
            holder.itemView.tv_quantity.text =
                listOfOrder.get(position).product_qty?: ""
            Glide.with(context!!).load(listOfOrder.get(position).product_image ?: "")
                .thumbnail(0.1f).placeholder(R.drawable.no_image)
                .into(holder.itemView.iv_product_photo)
        }
    }


    fun getItem(position: Int): OrderListResponse.OrderData? {
        return listOfOrder.get(position)
    }


}
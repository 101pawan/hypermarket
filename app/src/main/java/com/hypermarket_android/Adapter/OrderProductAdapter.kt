package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.AddOrderResponse
import kotlinx.android.synthetic.main.order_product_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderProductAdapter(
    private val context: FragmentActivity?,
    private val orderProductData: ArrayList<AddOrderResponse.OrderProduct>,
    val expectedDelivery: Long
) :
    RecyclerView.Adapter<OrderProductAdapter.MyViewHolder>() {


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.order_product_item, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return orderProductData.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(orderProductData[position].product_name!!.isNotEmpty()){
            holder.itemView.tv_name.text = orderProductData[position].product_name?.get(0)?.name ?: ""
            val price = orderProductData[position].product_price ?: ""
            holder.itemView.tv_selling_price.text = "$price AED"
            holder.itemView.tv_quantity.text = orderProductData[position].product_qty ?: ""
            Glide.with(context!!).load(orderProductData[position].product_name?.get(0)?.main_image ?:"")
                .into(holder.itemView.iv_product_photo)
        }
        holder.itemView.simpleRatingBar.rating = orderProductData[position].rating!!.toFloat()
        holder.itemView.tv_rating.text = orderProductData[position].rating

        if (position == (orderProductData.size - 1)) {
            holder.itemView.expected_layout.visibility = View.VISIBLE
        } else {
            holder.itemView.expected_layout.visibility = View.GONE
        }
//        holder.itemView.tv_delivery_date.text = convertDate(expectedDelivery.toString())
        holder.itemView.tv_delivery_date.text = "2-3${context?.getString(R.string.days)}"
    }

    fun getItem(position: Int): AddOrderResponse.OrderProduct {
        return orderProductData[position]
    }

    private fun convertDate(
        dateInMilliseconds: String
    ): String? {
        val formatter = SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(Date(dateInMilliseconds.toLong()))
    }
}
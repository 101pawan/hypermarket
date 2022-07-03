package com.hypermarket_android.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.util.convertExpectedDelivery
import kotlinx.android.synthetic.main.order_product_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderProductAdapterTrack(
    private val context: FragmentActivity?,
    private val orderProductData: List<OrderListResponse.OrderData?>,
    val expectedDelivery: Long
) :
    RecyclerView.Adapter<OrderProductAdapterTrack.MyViewHolder>() {


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
        return orderProductData!!.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(orderProductData!![position]?.product_name!!.isNotEmpty()){
            holder.itemView.tv_name.text = orderProductData[position]?.name ?: ""
            Glide.with(context!!)
                .load(orderProductData[position]?.main_image ?: "")
                .placeholder(R.drawable.no_image)
                .into(holder.itemView.iv_product_photo)
            holder.itemView.tv_selling_price.text =
                orderProductData[position]?.product_price+" AED"
            holder.itemView.tv_quantity.text =
                orderProductData[position]?.product_qty ?: ""
        }
        holder.itemView.simpleRatingBar.rating = orderProductData[position]?.rating!!.toFloat()
        holder.itemView.tv_rating.text = orderProductData[position]?.rating
        if (position == (orderProductData.size - 1)) {
            holder.itemView.expected_layout.visibility = View.VISIBLE
        } else {
            holder.itemView.expected_layout.visibility = View.GONE
        }
        holder.itemView.tv_delivery_date.text = convertExpectedDelivery(expectedDelivery * 1000)
    }

    fun getItem(position: Int): OrderListResponse.OrderData {
        return orderProductData!![position]!!
    }

    private fun convertDate(
        dateInMilliseconds: String
    ): String? {
        val formatter = SimpleDateFormat("dd-MMMM-yyyy");
        return formatter.format(Date(dateInMilliseconds))
    }
}
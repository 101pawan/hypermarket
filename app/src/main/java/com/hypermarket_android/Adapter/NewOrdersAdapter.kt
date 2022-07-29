package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.OrderTrackActivity
import com.hypermarket_android.dataModel.OrderListResponse
import kotlinx.android.synthetic.main.new_products_order_item_view.view.*


class NewOrdersAdapter (
    private val context: FragmentActivity?,
    private val listOfOrder: List<OrderListResponse.OrderData?>,
    private val completeListOfOrder: List<OrderListResponse.OrderData>?,
    private val selected_position: Int,
    private val fragment_position: Int,
    private val listener: OnclickListener
) :
    RecyclerView.Adapter<NewOrdersAdapter.MyViewHolder>() {

    interface OnclickListener {
        fun onClick(selected_position: Int,indexPosition: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.new_products_order_item_view, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder?.size?:0
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (fragment_position == 4){
            holder.itemView.tv_track_order.visibility = View.GONE
            holder.itemView.trackView.visibility = View.GONE
        }
        else{
            holder.itemView.tv_track_order.visibility = View.VISIBLE
            holder.itemView.trackView.visibility = View.VISIBLE
        }
        holder.itemView.tv_rating.text = listOfOrder?.get(position)?.rating ?: ""
        holder.itemView.simpleRatingBar.rating = listOfOrder?.get(position)?.rating!!.toFloat()
        if(listOfOrder.get(position)!!.product_name!!.isEmpty()){
            holder.itemView.tv_name.text = "NA"
        }else{
            holder.itemView.tv_name.text = listOfOrder?.get(position)?.name ?: ""
            Glide.with(context!!).load(listOfOrder?.get(position)!!.product_image ?: "")
                .thumbnail(0.1f).placeholder(R.drawable.no_image)
                .into(holder.itemView.iv_product_photo)
        }
        holder.itemView.orderId.text = listOfOrder?.get(position)?.order_id

        val product_qty = (listOfOrder?.get(position)?.product_qty)?.toInt()
        val available_qty = (listOfOrder?.get(position)?.available_qty)?.toInt().let { it }
        val available_for_return = (listOfOrder?.get(position)?.available_for_return)?.toInt().let { it }
        val returned = (listOfOrder?.get(position)?.returned)?.toInt().let { it }
        val is_returnable = (listOfOrder?.get(position)?.is_returnable)?.toBoolean().let { it }

        available_for_return?.let {
            if (it > 0) {
                holder.itemView.action_layout.visibility = View.VISIBLE
                holder.itemView.availability.text = "Available Qty: $it"
                context?.resources?.let { it1 -> holder.itemView.availability.setTextColor(it1.getColor(R.color.green_text)) }
            }else{
                holder.itemView.action_layout.visibility = View.GONE
                holder.itemView.availability.text = "Not Available"
                context?.resources?.let { it1 -> holder.itemView.availability.setTextColor(it1?.getColor(R.color.red)) }
            }
        }

        if (is_returnable != true){
            holder.itemView.action_layout.visibility = View.GONE
        }

        val returned_qty = (listOfOrder.get(position)!!.available_qty)?.toInt()?.let {
            (listOfOrder.get(position)!!.product_qty)?.toInt()
                ?.minus(it)
        }

//        holder.itemView.returned_qty.text = (returned_qty).toString()
        holder.itemView.returned_qty.text = (returned).toString()
        holder.itemView.available_qty.text = (product_qty).toString()
        holder.itemView.modify_order.setOnClickListener {
            listener.onClick(selected_position,position)
//            openModifyOrder()
        }
        holder.itemView.tv_track_order.setOnClickListener {
            OrderTrackActivity.addOrderResponse = completeListOfOrder?.get(selected_position)
            context?.startActivity(Intent(context, OrderTrackActivity::class.java))
        }
    }

    fun getItem(position: Int): OrderListResponse.OrderData? {
        return listOfOrder?.get(position)
    }


}
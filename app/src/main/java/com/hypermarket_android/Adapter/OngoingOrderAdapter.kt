package com.app.pharmadawa.ui.notification

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.Adapter.ProductsOrderItemAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.OrderTrackActivity
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.util.convertExpectedDelivery
import kotlinx.android.synthetic.main.item_ongoing_order_list.view.*

class OngoingOrderAdapter(
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<OrderListResponse.OrderData>,
    private val listener: OnclickListener
) :
    RecyclerView.Adapter<OngoingOrderAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener


    interface OnclickListener {
        fun onClick(orderData: OrderListResponse.OrderData)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_ongoing_order_list, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.itemView.tv_delivery_date.text =
            convertExpectedDelivery((listOfOrder[position].expected_delivery?.toLong()))
        holder.itemView.tv_order_date.text =
            convertDate(listOfOrder[position].created_at!!.split(".")[0]!!)
        holder.itemView.tv_order_id.text = "#" + listOfOrder[position].order_id
        holder.itemView.tv_user_name.text = listOfOrder[position].name
        holder.itemView.tv_address_detail.text =
            " ${listOfOrder[position].house_number ?: ""}, ${listOfOrder[position].building_name ?: ""}, ${listOfOrder[position].street ?: ""} "


        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
            context,
            listOfOrder
        )

        holder.itemView.tv_track_order.setOnClickListener {
            OrderTrackActivity.addOrderResponse = listOfOrder[position]
            context?.startActivity(Intent(context, OrderTrackActivity::class.java))
        }


        holder.itemView.tv_address.text = listOfOrder[position].mobile_number


        holder.itemView.tv_cancel.setOnClickListener {

            listener.onClick(listOfOrder[position])
        }

    }


    fun getItem(position: Int): OrderListResponse.OrderData {
        return listOfOrder[position]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }


}
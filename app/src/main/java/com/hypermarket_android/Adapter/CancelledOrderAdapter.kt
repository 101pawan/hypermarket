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
import com.hypermarket_android.activity.OrderCancelledActivity
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.util.convertExpectedDelivery
import com.hypermarket_android.util.convertNewDate
import kotlinx.android.synthetic.main.item_cancelled_order_list.view.*
import kotlinx.android.synthetic.main.item_cancelled_order_list.view.products_recyclerview
import kotlinx.android.synthetic.main.item_cancelled_order_list.view.tv_order_date
import kotlinx.android.synthetic.main.item_cancelled_order_list.view.tv_order_id

class CancelledOrderAdapter(
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<OrderListResponse.OrderData>,
    private val listener: OnclickListener
) :
    RecyclerView.Adapter<CancelledOrderAdapter.MyViewHolder>() {
    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private var isBottomError: Boolean = false
    private var isBottomLoading: Boolean = false
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_cancelled_order_list, p0, false)
        return MyViewHolder(view)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tv_order_id.text = "#" + listOfOrder[position].order_id
//        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
//            context,
//            listOfOrder[position].order_products!!
//        )
        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
            context,
            listOfOrder
        )
        holder.itemView.tv_delivery_date.text =
            convertExpectedDelivery((listOfOrder[position].expected_delivery?.toLong()))
        holder.itemView.tv_order_date.text = convertNewDate(listOfOrder[position].created_at!!.split(".")[0])
        holder.itemView.order_cancelled.setOnClickListener {
            OrderCancelledActivity.addOrderResponse = listOfOrder[position]
            context!!.startActivity(Intent(context, OrderCancelledActivity::class.java))
        }
        if (position == listOfOrder.size - 1 && !isBottomError && !isBottomLoading) {
            onBottomReachedListener.onBottomReached(position)
        }
        //  holder.itemView.setOnClickListener { listener.onClick(listOfOrder[position]) }
    }


    fun getItem(position: Int): OrderListResponse.OrderData {
        return listOfOrder[position]
    }

    interface OnclickListener {
        fun onClick(orderData: OrderListResponse.OrderData)

    }

}
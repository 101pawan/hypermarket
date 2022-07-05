package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.pharmadawa.ui.notification.OngoingOrderAdapter
import com.hypermarket_android.R
import com.hypermarket_android.activity.OrderTrackActivity
import com.hypermarket_android.dataModel.GetOrdersList
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.listener.OnBottomReachedListener
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.util.convertExpectedDelivery
import kotlinx.android.synthetic.main.item_ongoing_order_list.view.*
import kotlinx.android.synthetic.main.orderlistlayout.view.*
import java.lang.Exception

class OrdersListAdapter(
    private val context: FragmentActivity?,
    private val listOfOrder: ArrayList<GetOrdersList.OrderData>,
    private val listener: OnclickListener
) :
RecyclerView.Adapter<OrdersListAdapter.MyViewHolder>() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener
    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private var isBottomError: Boolean = false
    private var isBottomLoading: Boolean = false

    interface OnclickListener {
        fun onClick(orderData: GetOrdersList.OrderData,position: Int)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.orderlistlayout, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        try {
            holder.itemView.date.text = listOfOrder[position].order_date
//            holder.itemView.date.text =
//                convertExpectedDelivery((listOfOrder[position].expected_delivery?.toLong()))
        }catch (e:Exception){}
//        holder.itemView.tv_order_date.text =
//            convertDate(listOfOrder[position].created_at!!.split(".")[0]!!)
        val order = listOfOrder[position]
        holder.itemView.order_id.text = "#" + listOfOrder[position].order_id
        holder.itemView.price.text = "#" + listOfOrder[position].total_payable_amount +" "+context?.getString(R.string.aed)
//        holder.itemView.tv_user_name.text = listOfOrder[position].shipping_detail!!.name
//        holder.itemView.phone.text = listOfOrder[position].alt_mobile_number
//        holder.itemView.tv_address_detail.text =
//            " ${listOfOrder[position].shipping_detail!!.house_number ?: ""}, ${listOfOrder[position].shipping_detail!!.building_name ?: ""}, ${listOfOrder[position].shipping_detail!!.street ?: ""} "


//        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
//            context,
//            listOfOrder[position].order_products!!
//        )

//        holder.itemView.tv_track_order.setOnClickListener {
//            OrderTrackActivity.addOrderResponse = listOfOrder[position]
//            context?.startActivity(Intent(context, OrderTrackActivity::class.java))
//        }


//        holder.itemView.tv_address.text = listOfOrder[position].shipping_detail!!.mobile_number


        holder.itemView.order_layout.setOnClickListener {
            listener.onClick(order,position)
        }
        if (position == listOfOrder.size - 1 && !isBottomError && !isBottomLoading) {
            onBottomReachedListener.onBottomReached(position)
        }

    }


    fun getItem(position: Int): GetOrdersList.OrderData {
        return listOfOrder[position]
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }


}
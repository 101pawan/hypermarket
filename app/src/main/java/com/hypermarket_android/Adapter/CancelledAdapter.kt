package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hypermarket_android.R
import com.hypermarket_android.activity.ReviewActivity
import com.hypermarket_android.dataModel.GetOrdersList
import com.hypermarket_android.listener.OnBottomReachedListener
import kotlinx.android.synthetic.main.cancelledadpter_layout.view.*
import java.lang.Exception

class CancelledAdapter(
    private val context: FragmentActivity?,
    private var listOfOrder: ArrayList<GetOrdersList.OrderData>,
    val listener: OnclickListener
) :
    RecyclerView.Adapter<CancelledAdapter.MyViewHolder>() {
    private lateinit var onBottomReachedListener: OnBottomReachedListener
    private var isBottomError: Boolean = false
    private var isBottomLoading: Boolean = false

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cancelledadpter_layout, p0, false)
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


        holder.itemView.tv_order_id.text = "#" + listOfOrder[position].order_id
        /* holder.itemView.tv_user_name.text = listOfOrder[position].shipping_detail!!.name
         holder.itemView.tv_address_detail.text =
             " ${listOfOrder[position].shipping_detail!!.house_number ?: ""}, ${listOfOrder[position].shipping_detail!!.building_name ?: ""}, ${listOfOrder[position].shipping_detail!!.street ?: ""} "
 */
        try {
//            holder.itemView.tv_delivery_date.text =
//                convertExpectedDelivery((listOfOrder[position].expected_delivery?.toLong()))
            holder.itemView.tv_delivery_date.text = "2- 3 days"
        } catch (e: Exception) {

        }
        holder.itemView.tv_name.text = listOfOrder[position].order_id
//        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
//            context,
//            listOfOrder
//        )
//        holder.itemView.tv_order_date.text = convertNewDate(listOfOrder[position].order_date!!.split(".")[0]!!)
        holder.itemView.tv_order_date.text = listOfOrder[position].order_date
        Glide.with(context!!).load(getItem(position)?.product_image)
            .placeholder(R.drawable.no_image)
            .into(holder.itemView.iv_product_photo)
        holder.itemView.give_Rating.setOnClickListener {
            ReviewActivity.productImage =
                listOfOrder[position].product_image!!
//            ReviewActivity.productName =
//                listOfOrder[position].name!!
            ReviewActivity.product_id =
                listOfOrder[position].order_id!!
            context!!.startActivity(Intent(context, ReviewActivity::class.java))
        }

        holder.itemView.past_order.setOnClickListener {
            listener.onClickItem(listOfOrder[position], position)
        }
        holder.itemView.download_invoice.setOnClickListener {

            listener.onClick(listOfOrder[position])

        }
        if (position == listOfOrder.size - 1 && !isBottomError && !isBottomLoading) {
            onBottomReachedListener.onBottomReached(position)
        }
    }


    fun getItem(position: Int): GetOrdersList.OrderData {
        return listOfOrder[position]
    }

    interface OnclickListener {
        fun onClickItem(orderData: GetOrdersList.OrderData, position: Int)
        fun onClick(orderData: GetOrdersList.OrderData)
    }
}
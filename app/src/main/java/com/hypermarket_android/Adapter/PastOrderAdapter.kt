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
import com.hypermarket_android.activity.ReviewActivity
import com.hypermarket_android.dataModel.OrderListResponse
import com.hypermarket_android.util.convertDate
import com.hypermarket_android.util.convertExpectedDelivery
import kotlinx.android.synthetic.main.item_past_order_list.view.*
import kotlinx.android.synthetic.main.item_past_order_list.view.tv_order_date

class PastOrderAdapter(
    private val context: FragmentActivity?,
    private var listOfOrder: ArrayList<OrderListResponse.OrderData>,
    val listener: OnclickListener
) :
    RecyclerView.Adapter<PastOrderAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    //Inflate view for recycler
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_past_order_list, p0, false)
        return MyViewHolder(view)
    }

    //Return size
    override fun getItemCount(): Int {
        return listOfOrder.size
    }

    //Bind View Holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.tv_order_id.text = "#" + listOfOrder[position].order_id
       /* holder.itemView.tv_user_name.text = listOfOrder[position].shipping_detail!!.name
        holder.itemView.tv_address_detail.text =
            " ${listOfOrder[position].shipping_detail!!.house_number ?: ""}, ${listOfOrder[position].shipping_detail!!.building_name ?: ""}, ${listOfOrder[position].shipping_detail!!.street ?: ""} "
*/
        holder.itemView.tv_delivery_date.text =
            convertExpectedDelivery((listOfOrder[position].expected_delivery?.toLong()))
        holder.itemView.products_recyclerview.adapter = ProductsOrderItemAdapter(
            context,
            listOfOrder
        )
        holder.itemView.tv_order_date.text = convertDate(listOfOrder[position].created_at!!.split(".")[0]!!)

        holder.itemView.give_Rating.setOnClickListener {
            ReviewActivity.productImage =
                listOfOrder[position].main_image!!
            ReviewActivity.productName =
                listOfOrder[position].name!!
            ReviewActivity.product_id =
                listOfOrder[position].id!!
            context!!.startActivity(Intent(context, ReviewActivity::class.java))
        }


        holder.itemView.download_invoice.setOnClickListener {

            listener.onClick(listOfOrder[position])
        }
    }


    fun getItem(position: Int): OrderListResponse.OrderData {
        return listOfOrder[position]
    }

    interface OnclickListener {
        fun onClick(orderData: OrderListResponse.OrderData)
    }


}